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
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.duration._
import actors.events.ImageUploadEvent

class ImageUploadActor @Inject() (dao: AyaneruDAO) extends PersistentActor with AtLeastOnceDelivery {
  override def persistenceId = "image-upload-actor"

  def receiveRecover: Receive = {
    case u: ImageUploadEvent.Upload => {
      Logger.debug(s"recoverd upload: ${u.id}")
      execute(u)
      saveSnapshot(1)
    }
    case SnapshotOffer(_, snapshot: Int) => ()
  }

  def receiveCommand: Receive = {
    case u: ImageUploadEvent.Upload => persist(u) { x =>
      execute(u)
      saveSnapshot(1)
      sender ! "uploaded"
    }
    case "snapshot" => saveSnapshot(1)
  }

  def execute(upload: ImageUploadEvent.Upload):Boolean = {
    val ayaneru = dao.findById(upload.id)
    Logger.info(ayaneru.toJson.prettyPrint)
    ayaneru match {
      case Some(aya) => {
        val uploader = new ImageUploader(aya.originalURL)
        val f: Future[Option[String]] = for {
          (name, path) <- uploader.download()
          up <- uploader.upload(name, path)
        } yield up
        Await.result(f, 10 seconds)
        for (res <- f.value) res match {
          case Success(r) => {
            r.map {Logger.info(_)}
            dao.update(aya.copy(imageURL = r))
          }
          case Failure(r) => false
        }
        true
      }
      case None => false
    }
  }
}
