from fastapi import APIRouter
from app.api.v1 import auth, cities, spheres, news, users, images

api_router = APIRouter(prefix="/api/v1")

api_router.include_router(auth.router)
api_router.include_router(cities.router)
api_router.include_router(spheres.router)
api_router.include_router(news.router)
api_router.include_router(users.router)
api_router.include_router(images.router)

