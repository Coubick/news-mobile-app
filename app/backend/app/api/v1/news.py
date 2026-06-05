from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.orm import Session
from sqlalchemy import and_
from app.db.database import get_db
from app.models import News, User
from app.schemas.news import NewsCreate, NewsUpdate, NewsResponse, NewsListResponse
from app.auth.security import get_current_user

router = APIRouter(prefix="/news", tags=["news"])


@router.get("/", response_model=NewsListResponse)
def get_news(
    city_id: int = Query(None),
    sphere_id: int = Query(None),
    limit: int = Query(20, ge=1, le=100),
    offset: int = Query(0, ge=0),
    db: Session = Depends(get_db),
):
    """
    Получить список новостей с фильтрацией по городам и сферам.
    """
    query = db.query(News).filter(News.deleted_at.is_(None))

    # Применяем фильтры
    filters = []
    if city_id is not None:
        filters.append(News.city_id == city_id)
    if sphere_id is not None:
        filters.append(News.sphere_id == sphere_id)

    if filters:
        query = query.filter(and_(*filters))

    # Сортируем по дате создания (новые сверху)
    query = query.order_by(News.created_at.desc())

    # Получаем общее количество
    total = query.count()

    # Применяем пагинацию
    items = query.offset(offset).limit(limit).all()

    return NewsListResponse(total=total, items=items)


@router.post("/", response_model=NewsResponse)
def create_news(
    news_data: NewsCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    """
    Создать новую новость.
    """
    new_news = News(
        user_id=current_user.id,
        title=news_data.title,
        description=news_data.description,
        image_url=news_data.image_url,
        city_id=news_data.city_id,
        sphere_id=news_data.sphere_id,
    )

    db.add(new_news)
    db.commit()
    db.refresh(new_news)

    return new_news


@router.get("/{news_id}", response_model=NewsResponse)
def get_news_by_id(
    news_id: int,
    db: Session = Depends(get_db),
):
    """
    Получить одну новость по ID.
    """
    news = db.query(News).filter(
        and_(News.id == news_id, News.deleted_at.is_(None))
    ).first()

    if not news:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="News not found",
        )

    return news


@router.put("/{news_id}", response_model=NewsResponse)
def update_news(
    news_id: int,
    news_data: NewsUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    """
    Редактировать новость (только автор может редактировать).
    """
    news = db.query(News).filter(
        and_(News.id == news_id, News.deleted_at.is_(None))
    ).first()

    if not news:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="News not found",
        )

    # Проверяем, что текущий пользователь - автор новости
    if news.user_id != current_user.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You can only edit your own news",
        )

    # Обновляем поля
    if news_data.title is not None:
        news.title = news_data.title
    if news_data.description is not None:
        news.description = news_data.description
    if news_data.image_url is not None:
        news.image_url = news_data.image_url
    if news_data.city_id is not None:
        news.city_id = news_data.city_id
    if news_data.sphere_id is not None:
        news.sphere_id = news_data.sphere_id

    db.commit()
    db.refresh(news)

    return news


@router.delete("/{news_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_news(
    news_id: int,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    """
    Удалить новость (мягкое удаление, только автор может удалять).
    """
    news = db.query(News).filter(
        and_(News.id == news_id, News.deleted_at.is_(None))
    ).first()

    if not news:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="News not found",
        )

    # Проверяем, что текущий пользователь - автор новости
    if news.user_id != current_user.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You can only delete your own news",
        )

    # Выполняем мягкое удаление
    from datetime import datetime, timezone
    news.deleted_at = datetime.now(timezone.utc)
    db.commit()
