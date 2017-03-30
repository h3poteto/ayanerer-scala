package actors

import play.api.Logger
import akka.actor._
import dao.AyaneruDAO
import spray.json._
import models.AyaneruJsonProtocol._
import scala.concurrent.ExecutionContext.Implicits.global

object ImageUploadActor {
  case class Upload(id: Int, dao: AyaneruDAO)
}

class ImageUploadActor extends Actor {
  import ImageUploadActor._

  def receive = {
    case Upload(id, dao) =>
      execute(id, dao)
  }

  def execute(id: Int, dao: AyaneruDAO) {
    dao.findById(id).map { ayaneru =>
      Logger.info(ayaneru.toJson.prettyPrint)
    }
  }
}
