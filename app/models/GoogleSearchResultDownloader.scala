package models

import spray.json._
import dispatch._
import javax.inject.Inject
import dao.AyaneruDAOImpl
import models.GoogleSearchResponseJsonProtocol._
import actors.ImageUploadActor
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Success, Failure}
import play.api.Logger

class GoogleSearchResultDownloader @Inject()(val request: GoogleSearchRequest, system: ActorSystem) {
  def download() = {
    val items = search().items
    items match {
      case Some(List(_*)) => {
        items.get.map { item =>
          val id = saveImage(item)
          id match {
            case Some(id) => {
              val actor = system.actorOf(Props[ImageUploadActor])
              val timeout = Timeout(30 seconds)
              val f: Future[String] = (actor ask ImageUploadActor.Upload(id.toInt, new AyaneruDAOImpl))(timeout).mapTo[String]
              Await.ready(f, Duration.Inf)
              f.value.get match {
                case Success(r) => Logger.info(s"Success to download: $r")
                case Failure(r) => Logger.error(s"Failed to download: $r")
              }
            }
            case None => {}
          }
        }
      }
      case None => ()
    }
  }

  def search(): GoogleSearchResponse = {
    val result = request.request().apply()
    //Logger.debug(result)
    result.parseJson.convertTo[GoogleSearchResponse]
  }

  def saveImage(item: GoogleSearchItem): Option[Long] = {
    val ayaneru = new Ayaneru(None, item.link)
    val dao = new AyaneruDAOImpl
    dao.create(ayaneru)
  }
}

