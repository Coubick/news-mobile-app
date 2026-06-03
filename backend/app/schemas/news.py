from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime
from app.schemas.user import UserResponse
from app.schemas.city import CityResponse
from app.schemas.sphere import SphereResponse


class NewsBase(BaseModel):
    title: str = Field(..., min_length=1, max_length=512)
    description: str = Field(..., min_length=1)
    city_id: int
    sphere_id: int


class NewsCreate(NewsBase):
    image_url: Optional[str] = None


class NewsUpdate(BaseModel):
    title: Optional[str] = Field(None, min_length=1, max_length=512)
    description: Optional[str] = Field(None, min_length=1)
    image_url: Optional[str] = None
    city_id: Optional[int] = None
    sphere_id: Optional[int] = None


class NewsResponse(NewsBase):
    id: int
    user_id: int
    image_url: Optional[str]
    created_at: datetime
    updated_at: datetime
    user: UserResponse
    city: CityResponse
    sphere: SphereResponse

    class Config:
        from_attributes = True


class NewsListResponse(BaseModel):
    total: int
    items: list[NewsResponse]
