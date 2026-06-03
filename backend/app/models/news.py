from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey, func
from sqlalchemy.orm import relationship
from app.db.database import Base


class News(Base):
    __tablename__ = "news"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
    title = Column(String(512), nullable=False, index=True)
    description = Column(Text, nullable=False)
    image_url = Column(String(512), nullable=True)
    city_id = Column(Integer, ForeignKey("cities.id"), nullable=False)
    sphere_id = Column(Integer, ForeignKey("spheres.id"), nullable=False)
    created_at = Column(DateTime, server_default=func.now(), index=True)
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    # Relationships
    user = relationship("User")
    city = relationship("City")
    sphere = relationship("Sphere")

    def __repr__(self):
        return f"<News(id={self.id}, title={self.title})>"
