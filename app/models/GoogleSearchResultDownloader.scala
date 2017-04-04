package models

import spray.json._
import dispatch._
import javax.inject.Inject
import dao.AyaneruDAOImpl
import models.GoogleSearchResponseJsonProtocol._
import actors.ImageUploadActor
import akka.actor._
import play.api.Logger

class GoogleSearchResultDownloader @Inject()(val request: GoogleSearchRequest, system: ActorSystem) {
  def download() = {
    val items = search().items
    items match {
      case Some(List(_*)) => {
        items.get.map { item =>
          val id = saveImage(item)
          val actor = system.actorOf(Props[ImageUploadActor])
          actor ! ImageUploadActor.Upload(id.toInt, new AyaneruDAOImpl)
        }
      }
      case None => ()
    }
  }

  def search(): GoogleSearchResponse = {
    val result = request.request().apply()
    Logger.debug(result)
    result.parseJson.convertTo[GoogleSearchResponse]
  }

  def saveImage(item: GoogleSearchItem): Long = {
    val ayaneru = new Ayaneru(None, item.link)
    val dao = new AyaneruDAOImpl
    dao.create(ayaneru)
  }
}

