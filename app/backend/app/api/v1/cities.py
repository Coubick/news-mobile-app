from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.models import City
from app.schemas.city import CityResponse

router = APIRouter(prefix="/cities", tags=["cities"])


@router.get("/", response_model=list[CityResponse])
def get_cities(db: Session = Depends(get_db)):
    """
    Получить список всех городов.
    """
    cities = db.query(City).all()
    return cities
