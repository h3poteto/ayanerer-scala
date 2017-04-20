package actors

import play.api.Logger
import play.api.Play
import akka.actor._
import akka.persistence._
import dao.AyaneruDAO
import javax.inject.Inject
import models.AyaneruJsonProtocol._
import models.{Ayaneru, ImageUploader}
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.duration._
import stamina.Persistable
import stamina.json.persister
import spray.json._


object ImageUploaderActorJsonProtocol extends DefaultJsonProtocol {
  import ImageUploadActor._
  implicit val format = jsonFormat1(Upload.apply)
}


object ImageUploadActor {
  import ImageUploaderActorJsonProtocol._
  case class Upload(id: Int) extends Persistable
  val v1ImageUploaderPersister = persister[Upload]("image-uploader")
}

class ImageUploadActor @Inject() (dao: AyaneruDAO) extends PersistentActor with AtLeastOnceDelivery {
  import ImageUploadActor._
  override def persistenceId = "image-upload-actor"

  def receiveRecover: Receive = {
    case u: Upload => {
      Logger.debug(s"recoverd upload: ${u.id}")
      execute(u)
      saveSnapshot(1)
    }
    case SnapshotOffer(_, snapshot: Int) => ()
  }

  def receiveCommand: Receive = {
    case u: Upload => persist(u) { x =>
      execute(u)
      saveSnapshot(1)
      sender ! "uploaded"
    }
    case "snapshot" => saveSnapshot(1)
  }

  def execute(upload: Upload):Boolean = {
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
