from fastapi import APIRouter, Depends
from app.auth.security import get_current_user
from app.models import User
from app.schemas.image import CloudinarySignatureResponse
from app.services.cloudinary_service import CloudinaryService

router = APIRouter(prefix="/images", tags=["images"])


@router.post("/generate-signature", response_model=CloudinarySignatureResponse)
def generate_cloudinary_signature(current_user: User = Depends(get_current_user)):
    """
    Генерирует подпись для загрузки изображений на Cloudinary.
    
    Подпись необходима для того, чтобы клиент мог загружать файлы
    напрямую на Cloudinary без отправки через сервер.
    """
    signature_data = CloudinaryService.generate_upload_signature()
    return CloudinarySignatureResponse(**signature_data)
