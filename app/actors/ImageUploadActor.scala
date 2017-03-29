package actors

import javax.inject._
import play.api.Logger
import akka.actor._
import dao.AyaneruDAO
import spray.json._
import models.AyaneruJsonProtocol._
import scala.concurrent.ExecutionContext.Implicits.global

object ImageUploadActor {
  case class Upload(id: Int)
}

class ImageUploadActor @Inject() (dao: AyaneruDAO) extends Actor {
  import ImageUploadActor._

  def receive = {
    case Upload(id) =>
      execute(id)
  }

  def execute(id: Int) {
    dao.findById(id).map { ayaneru =>
      Logger.info(ayaneru.toJson.prettyPrint)
    }
    Logger.info("called")

  }
}
