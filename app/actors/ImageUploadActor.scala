package actors

import play.api.Logger
import akka.actor._
import dao.AyaneruDAO
import spray.json._
import models.AyaneruJsonProtocol._
import models.{Ayaneru, ImageUploader}

object ImageUploadActor {
  case class Upload(id: Int, dao: AyaneruDAO)
}

class ImageUploadActor extends Actor {
  import ImageUploadActor._

  def receive = {
    case Upload(id, dao) =>
      execute(id, dao)
  }

  def execute(id: Int, dao: AyaneruDAO):Boolean = {
    val ayaneru = dao.findById(id)
    Logger.info(ayaneru.toJson.prettyPrint)
    ayaneru match {
      case Some(Ayaneru(_,_)) => {
        val aya = ayaneru.get
        val uploader = new ImageUploader(aya.image)
        println(uploader.download())
        true
      }
      case None => false
    }
  }
}
