from pydantic import BaseModel, Field


class SphereBase(BaseModel):
    name: str = Field(..., min_length=1, max_length=255)


class SphereCreate(SphereBase):
    pass


class SphereResponse(SphereBase):
    id: int

    class Config:
        from_attributes = True
