package models

import spray.json._
import dispatch._
import javax.inject.{Inject, Named}
import dao.AyaneruDAO
import models.GoogleSearchResponseJsonProtocol._
import actors.events.ImageUploadEvent
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import play.api.Logger


class GoogleSearchResultDownloader @Inject()(val request: GoogleSearchRequest, @Named("imageUploader") actor: ActorRef, val dao: AyaneruDAO) {
  def download():Future[Option[List[Boolean]]] = {
    val items = search().items
    Future {
      for (list <- items) yield list.map { item =>
        val id = saveImage(item)
        id match {
          case Some(id) => {
            val timeout = Timeout(30 seconds)
            val f: Future[String] = (actor ask ImageUploadEvent.Upload(id.toInt))(timeout).mapTo[String]
            Await.ready(f, Duration.Inf)
            (for (res <- f.value) yield res match {
              case Success(r) => {
                Logger.info(s"Success to download: $r")
                true
              }
              case Failure(r) => {
                Logger.error(s"Failed to download: $r")
                false
              }
            }).getOrElse(false)
          }
          case None => false
        }
      }
    }
  }

  def search(): GoogleSearchResponse = {
    val result = request.request().apply()
    Logger.debug(result)
    result.parseJson.convertTo[GoogleSearchResponse]
  }

  def saveImage(item: GoogleSearchItem): Option[Long] = {
    val ayaneru = new Ayaneru(None, None, item.link)
    dao.create(ayaneru)
  }
}

