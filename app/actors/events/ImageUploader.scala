package actors.events

import stamina.Persistable
import stamina.json.persister
import spray.json._

object ImageUploadEvent extends DefaultJsonProtocol {
  case class Upload(id: Int) extends Persistable
  implicit val format = jsonFormat1(Upload.apply)
  val v1ImageUploaderPersister = persister[Upload]("image-uploader")
}
