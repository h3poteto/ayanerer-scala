package actors

import play.api.Logger
import play.api.Play
import akka.actor._
import akka.persistence._
import dao.AyaneruDAO
import spray.json._
import javax.inject.Inject
import models.AyaneruJsonProtocol._
import models.{Ayaneru, ImageUploader}

object ImageUploadActor {
  case class Upload(id: Int)
}

class ImageUploadActor @Inject() (dao: AyaneruDAO) extends PersistentActor with AtLeastOnceDelivery {
  import ImageUploadActor._
  override def persistenceId = "image-upload-actor"

  def receiveRecover: Receive = {
    case u: Upload => {
      Logger.debug(s"recoverd upload: ${u.id}")
      execute(u)
    }
    case SnapshotOffer(_, snapshot: Int) => ()
  }

  def receiveCommand: Receive = {
    case u: Upload => persist(u) { x =>
      execute(u)
      sender ! "uploaded"
    }
  }

  def execute(upload: Upload):Boolean = {
    val ayaneru = dao.findById(upload.id)
    Logger.info(ayaneru.toJson.prettyPrint)
    ayaneru match {
      case Some(Ayaneru(_,_)) => {
        val aya = ayaneru.get
        val uploader = new ImageUploader(aya.image)
        // TODO: キーをDBに保存しときたい
        println(uploader.upload())
        true
      }
      case None => false
    }
  }
}
