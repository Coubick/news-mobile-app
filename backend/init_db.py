"""
Скрипт для инициализации БД с тестовыми данными.
Запуск: python init_db.py
"""

from sqlalchemy.orm import Session
from app.db.database import SessionLocal, engine, Base
from app.models import City, Sphere, User, News
from datetime import datetime, timezone

print("🔄 Creating tables...")
Base.metadata.create_all(bind=engine)
print("✅ Tables created")

db = SessionLocal()

try:
    print("🔄 Clearing existing data...")
    db.query(News).delete()
    db.query(User).delete()
    db.query(City).delete()
    db.query(Sphere).delete()
    db.commit()

    print("🔄 Adding cities...")
    cities_data = [
        "Moscow",
        "Saint Petersburg",
        "Novosibirsk",
        "Yekaterinburg",
        "Nizhny Novgorod",
    ]
    cities = [City(name=name) for name in cities_data]
    db.add_all(cities)
    db.flush()  # ← ВАЖНО: flush чтобы получить ID
    db.commit()
    print(f"✅ Created {len(cities)} cities")

    print("🔄 Adding spheres...")
    spheres_data = [
        "Technology",
        "Health",
        "Education",
        "Business",
        "Entertainment",
        "Sports",
    ]
    spheres = [Sphere(name=name) for name in spheres_data]
    db.add_all(spheres)
    db.flush()  # ← ВАЖНО: flush чтобы получить ID
    db.commit()
    print(f"✅ Created {len(spheres)} spheres")

    print("🔄 Adding test users...")
    from app.auth.security import hash_password
    
    users_data = [
        {"username": "john_doe", "password": "password123"},
        {"username": "jane_smith", "password": "password456"},
    ]
    users = []
    for user_data in users_data:
        try:
            hashed = hash_password(user_data["password"])
            user = User(
                username=user_data["username"],
                password_hash=hashed,
            )
            users.append(user)
        except Exception as e:
            print(f"⚠️ Warning hashing password: {e}")
            user = User(
                username=user_data["username"],
                password_hash=user_data["password"],
            )
            users.append(user)
    
    db.add_all(users)
    db.flush()  # ← ВАЖНО: flush чтобы получить ID
    db.commit()
    print(f"✅ Created {len(users)} users")


    print("🔄 Adding test news...")
    news_data = [
        {
            "title": "New Python Framework Released",
            "description": "A new Python framework for building web applications has been released.",
            "user_id": users[0].id,
            "city_id": cities[0].id,  # ← Используем ID из объекта
            "sphere_id": spheres[0].id,  # ← Используем ID из объекта
        },
        {
            "title": "Latest Health Study",
            "description": "Recent study shows the benefits of regular exercise.",
            "user_id": users[1].id,
            "city_id": cities[1].id,  # ← Используем ID из объекта
            "sphere_id": spheres[1].id,  # ← Используем ID из объекта
        },
        {
            "title": "Education Innovation",
            "description": "New methods in online education are proving effective.",
            "user_id": users[0].id,
            "city_id": cities[2].id,  # ← Используем ID из объекта
            "sphere_id": spheres[2].id,  # ← Используем ID из объекта
        },
    ]
    news_items = [News(**news) for news in news_data]
    db.add_all(news_items)
    db.commit()
    print(f"✅ Created {len(news_items)} news")

    print("\n" + "="*50)
    print("✅ Database initialized successfully!")
    print("="*50)
    print("\nTest users:")
    for user in users_data:
        print(f"  - username: {user['username']}, password: {user['password']}")
    print("\n📍 Swagger UI: http://localhost:8000/docs")

except Exception as e:
    print(f"\n❌ Error initializing database: {e}")
    import traceback
    traceback.print_exc()
    db.rollback()

finally:
    db.close()