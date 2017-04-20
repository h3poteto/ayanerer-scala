package serializers

import stamina.{StaminaAkkaSerializer}
import actors.events.ImageUploadEvent

class ImageUploaderSerializer extends StaminaAkkaSerializer(ImageUploadEvent.v1ImageUploaderPersister)

