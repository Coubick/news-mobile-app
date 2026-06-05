from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.config import settings
from app.api.v1.routes import api_router
from app.db.database import Base, engine

# ✅ Автоматически создаём таблицы при запуске
Base.metadata.create_all(bind=engine)

app = FastAPI(
    title=settings.APP_NAME,
    description="News App API with registration, news feed, and filtering",
    version="1.0.0",
)

# CORS middleware для работы с Android клиентом
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # В production нужно ограничить!
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Подключаем все роуты
app.include_router(api_router)


@app.get("/")
def root():
    """
    Корневой endpoint для проверки что сервер работает.
    """
    return {
        "message": "News App API is running",
        "docs": "/docs",
        "redoc": "/redoc",
    }


@app.get("/health")
def health_check():
    """
    Проверка здоровья сервера.
    """
    return {"status": "ok"}