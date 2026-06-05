from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.orm import Session
from sqlalchemy import and_
from app.db.database import get_db
from app.models import User, News
from app.schemas.user import UserResponse, UserUpdate, UserDetailResponse
from app.schemas.news import NewsResponse, NewsListResponse
from app.auth.security import get_current_user, hash_password, verify_password

router = APIRouter(prefix="/users", tags=["users"])


@router.get("/me", response_model=UserDetailResponse)
def get_current_user_profile(current_user: User = Depends(get_current_user)):
    """
    Получить профиль текущего пользователя.
    """
    return current_user


@router.put("/me", response_model=UserDetailResponse)
def update_current_user(
    user_data: UserUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    """
    Обновить профиль текущего пользователя (username, пароль, аватар).
    """
    # Если меняем username, проверяем уникальность
    if user_data.username is not None:
        existing_user = db.query(User).filter(
            and_(User.username == user_data.username, User.id != current_user.id)
        ).first()
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Username already taken",
            )
        current_user.username = user_data.username

    # Если меняем пароль
    if user_data.password is not None:
        current_user.password_hash = hash_password(user_data.password)

    # Обновляем аватар
    if user_data.avatar_url is not None:
        current_user.avatar_url = user_data.avatar_url

    db.commit()
    db.refresh(current_user)

    return current_user


@router.get("/me/news", response_model=NewsListResponse)
def get_current_user_news(
    limit: int = Query(20, ge=1, le=100),
    offset: int = Query(0, ge=0),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    """
    Получить новости текущего пользователя.
    """
    query = db.query(News).filter(
        and_(News.user_id == current_user.id, News.deleted_at.is_(None))
    )

    # Сортируем по дате создания (новые сверху)
    query = query.order_by(News.created_at.desc())

    # Получаем общее количество
    total = query.count()

    # Применяем пагинацию
    items = query.offset(offset).limit(limit).all()

    return NewsListResponse(total=total, items=items)
