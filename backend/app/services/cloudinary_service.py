import cloudinary
from datetime import datetime, timedelta, timezone
from app.config import settings

cloudinary.config(
    cloud_name=settings.CLOUDINARY_CLOUD_NAME,
    api_key=settings.CLOUDINARY_API_KEY,
    api_secret=settings.CLOUDINARY_API_SECRET,
)


class CloudinaryService:
    @staticmethod
    def generate_upload_signature(folder: str = "news-app") -> dict:
        """
        Генерирует подпись для прямой загрузки на Cloudinary со стороны клиента.
        
        Args:
            folder: Папка в Cloudinary
            
        Returns:
            dict с signature, timestamp, folder, api_key и cloud_name
        """
        timestamp = int(
            (datetime.now(timezone.utc) + timedelta(hours=1)).timestamp()
        )

        # Параметры для подписи
        params_to_sign = f"folder={folder}&timestamp={timestamp}"

        # Создаём подпись
        import hashlib
        string_to_sign = params_to_sign + settings.CLOUDINARY_API_SECRET
        signature = hashlib.sha1(string_to_sign.encode()).hexdigest()

        return {
            "signature": signature,
            "timestamp": timestamp,
            "folder": folder,
            "api_key": settings.CLOUDINARY_API_KEY,
            "cloud_name": settings.CLOUDINARY_CLOUD_NAME,
        }

    @staticmethod
    def delete_image(public_id: str) -> bool:
        """
        Удаляет изображение из Cloudinary по public_id.
        
        Args:
            public_id: Public ID изображения в Cloudinary
            
        Returns:
            True если удаления успешно, False иначе
        """
        try:
            result = cloudinary.api.delete_resources([public_id])
            return result.get("deleted", {}).get(public_id) == "deleted"
        except Exception:
            return False
