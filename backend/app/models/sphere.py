from sqlalchemy import Column, Integer, String
from app.db.database import Base


class Sphere(Base):
    __tablename__ = "spheres"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), unique=True, nullable=False, index=True)

    def __repr__(self):
        return f"<Sphere(id={self.id}, name={self.name})>"
