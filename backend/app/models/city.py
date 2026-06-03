from sqlalchemy import Column, Integer, String
from app.db.database import Base


class City(Base):
    __tablename__ = "cities"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), unique=True, nullable=False, index=True)

    def __repr__(self):
        return f"<City(id={self.id}, name={self.name})>"
