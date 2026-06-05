from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.models import User
from app.schemas.user import UserRegister, UserLogin, UserResponse
from app.schemas.token import Token
from app.auth.security import (
    hash_password,
    verify_password,
    create_access_token,
)

router = APIRouter(prefix="/auth", tags=["auth"])


@router.post("/register", response_model=Token)
def register(user_data: UserRegister, db: Session = Depends(get_db)):
    """
    Регистрация нового пользователя.
    """
    # Проверяем, не существует ли пользователь с таким username
    existing_user = db.query(User).filter(User.username == user_data.username).first()
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Username already registered",
        )

    # Создаём нового пользователя
    hashed_password = hash_password(user_data.password)
    new_user = User(username=user_data.username, password_hash=hashed_password)

    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    # Возвращаем токен доступа
    access_token = create_access_token(user_id=new_user.id)
    return Token(access_token=access_token, user_id=new_user.id)


@router.post("/login", response_model=Token)
def login(user_data: UserLogin, db: Session = Depends(get_db)):
    """
    Логин пользователя.
    """
    # Ищем пользователя по username
    user = db.query(User).filter(User.username == user_data.username).first()
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid username or password",
        )

    # Проверяем пароль
    if not verify_password(user_data.password, user.password_hash):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid username or password",
        )

    # Возвращаем токен доступа
    access_token = create_access_token(user_id=user.id)
    return Token(access_token=access_token, user_id=user.id)
