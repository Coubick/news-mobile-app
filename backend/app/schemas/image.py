from pydantic import BaseModel


class CloudinarySignatureResponse(BaseModel):
    signature: str
    timestamp: int
    folder: str
    api_key: str
    cloud_name: str
