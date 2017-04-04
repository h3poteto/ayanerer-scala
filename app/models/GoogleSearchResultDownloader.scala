package models

import spray.json._
import dispatch._, Defaults._
import javax.inject.Inject
import dao.AyaneruDAOImpl
import models.GoogleSearchResponseJsonProtocol._
import actors.ImageUploadActor
import akka.actor._

class GoogleSearchResultDownloader @Inject()(val request: GoogleSearchRequest, system: ActorSystem) {
  def download() = {
    search().items.map { item =>
      val id = saveImage(item)
      val actor = system.actorOf(Props[ImageUploadActor])
      actor ! ImageUploadActor.Upload(id.toInt, new AyaneruDAOImpl)
    }
  }

  def search(): GoogleSearchResponse = {
    request.request().apply().parseJson.convertTo[GoogleSearchResponse]
  }

  def saveImage(item: GoogleSearchItem): Long = {
    val ayaneru = new Ayaneru(None, item.link)
    val dao = new AyaneruDAOImpl
    dao.create(ayaneru)
  }
}

