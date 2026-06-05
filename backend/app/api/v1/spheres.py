from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.models import Sphere
from app.schemas.sphere import SphereResponse

router = APIRouter(prefix="/spheres", tags=["spheres"])


@router.get("/", response_model=list[SphereResponse])
def get_spheres(db: Session = Depends(get_db)):
    """
    Получить список всех сфер.
    """
    spheres = db.query(Sphere).all()
    return spheres
