package serializers

import stamina.{StaminaAkkaSerializer}
import actors.ImageUploadActor

class ImageUploaderSerializer extends StaminaAkkaSerializer(ImageUploadActor.v1ImageUploaderPersister)

