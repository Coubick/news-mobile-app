from pydantic import BaseModel, Field


class CityBase(BaseModel):
    name: str = Field(..., min_length=1, max_length=255)


class CityCreate(CityBase):
    pass


class CityResponse(CityBase):
    id: int

    class Config:
        from_attributes = True

