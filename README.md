# News Mobile App
(На данный момент сервис недоступен для постоянного использования из-за ограничений по использованию Render.com и Cloudinary)

Мобильное приложение-новостной агрегатор с собственным бэкендом: пользовательская лента новостей с фильтрацией по городам и сферам, регистрация/вход, создание и редактирование новостей с загрузкой изображений.

Проект состоит из двух частей:

- **`android/`** — Android-клиент (Kotlin + Jetpack Compose), пакет `com.example.news`
- **`backend/`** — REST API (FastAPI + PostgreSQL + Cloudinary)

## Возможности

### Приложение (Android)
- 🔐 **Авторизация** — регистрация и вход, JWT-токен сохраняется локально (DataStore) и подставляется в заголовки запросов
- 📰 **Лента новостей** — список карточек (заголовок, описание, изображение, город, сфера) с пагинацией (limit/offset)
- 🔎 **Фильтры** — фильтрация ленты по городу и сфере (справочники загружаются с бэкенда)
- ➕ **Создание новостей** — форма с выбором города/сферы и загрузкой обложки через Cloudinary (подпись для загрузки выдаёт бэкенд)
- ✏️ **Редактирование и удаление** — изменение собственных новостей
- 👤 **Профиль** — данные пользователя, аватар, список своих новостей
- 🧭 **Навигация** — нижняя панель вкладок (Compose Navigation)

### Бэкенд (REST API `/api/v1`)
| Метод | Эндпоинт | Описание |
|---|---|---|
| POST | `/auth/register`, `/auth/login` | Регистрация и вход, выдача JWT |
| GET | `/news/` | Лента новостей с фильтрами `city_id`, `sphere_id`, `limit`, `offset` |
| GET/POST/PUT/DELETE | `/news/{id}` | CRUD новостей (мягкое удаление через `deleted_at`) |
| GET | `/cities/`, `/spheres/` | Справочники городов и сфер |
| GET/PUT | `/users/me` | Профиль пользователя |
| GET | `/users/me/news` | Новости пользователя |
| POST | `/images/generate-signature` | Подпись Cloudinary для загрузки изображений |

## 🛠 Технологии

**Android-клиент**
- Kotlin, Jetpack Compose (Material 3)
- Hilt (DI, KSP), Navigation Compose
- Retrofit + OkHttp (logging-interceptor), Gson; kotlinx.serialization
- Coil (загрузка изображений), Cloudinary Android SDK
- DataStore Preferences (хранение токена), Coroutines/Flow, MVVM

**Бэкенд**
- Python 3.11, FastAPI, Uvicorn
- SQLAlchemy 2.0 + PostgreSQL (psycopg2), Alembic
- JWT (python-jose), хеширование паролей (passlib/bcrypt)
- Pydantic v2 / pydantic-settings (конфигурация через `.env`)
- Cloudinary (хранение изображений), Docker

## 📁 Структура проекта

```
.
├── android/app/src/main/java/com/example/news/
│   ├── data/            # Retrofit API, DTO, TokenManager, реализации репозиториев
│   ├── di/              # Hilt-модули (NetworkModule, CloudinaryModule, RepositoryModule)
│   ├── domain/          # Модели домена и интерфейсы репозиториев
│   ├── presentation/    # Экраны Compose (auth, news, profile), ViewModels, навигация
│   └── utils/           # Constants, Result, вспомогательные классы
├── backend/
│   ├── app/
│   │   ├── api/v1/      # Роуты: auth, news, cities, spheres, users, images
│   │   ├── auth/        # Генерация/проверка JWT, hash паролей
│   │   ├── db/          # Сессия SQLAlchemy
│   │   ├── models/      # User, News, City, Sphere
│   │   ├── schemas/     # Pydantic-схемы запросов/ответов
│   │   ├── services/    # Cloudinary-сервис
│   │   └── main.py      # Точка входа FastAPI
│   ├── init_db.py       # Инициализация БД тестовыми данными
│   ├── jwt_secret_generator.py
│   ├── requirements.txt
│   └── Dockerfile
└── settings.gradle.kts  # Gradle-проект (root "news", модуль :app → android/app)
```

## ⚙️ Требования

- JDK 17+, Android Studio (Ladybug+), Android SDK 36 (minSdk 24)
- Python 3.11+, PostgreSQL 14+
- Аккаунт Cloudinary (для загрузки изображений)

## Запуск

### 1. Бэкенд

```bash
cd backend
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt

cp .env.example .env   # заполните DATABASE_URL, JWT_SECRET_KEY и Cloudinary-ключи
python init_db.py      # создаёт таблицы и заполняет справочники тестовыми данными

uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Swagger-документация доступна на `http://localhost:8000/docs`.

Через Docker:

```bash
docker build -f backend/Dockerfile -t news-api .
docker run --env-file backend/.env -p 8000:8000 news-api
```

### 2. Android-приложение

Адрес API задаётся в `android/app/src/main/java/com/example/news/di/NetworkModule.kt`
(константа `BASE_URL`; сейчас указан деплой на Render: `https://news-mobile-app-jgck.onrender.com/api/v1/`).
Для локального сервера с эмулятора используйте `http://10.0.2.2:8000/api/v1/`.

```bash
./gradlew :app:assembleDebug
adb install android/app/build/outputs/apk/debug/app-debug.apk
```
