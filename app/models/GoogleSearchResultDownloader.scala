package models

import spray.json._
import dispatch._, Defaults._
import javax.inject.Inject
import dao.AyaneruDAOImpl
import models.GoogleSearchResponseJsonProtocol._

class GoogleSearchResultDownloader @Inject()(val request: GoogleSearchRequest) {
  def download() = {
    search().items.map { item =>
      saveImage(item)
    }
  }

  def search(): GoogleSearchResponse = {
    request.request().apply().parseJson.convertTo[GoogleSearchResponse]
  }

  def saveImage(item: GoogleSearchItem) = {
    val ayaneru = new Ayaneru(None, item.link)
    val dao = new AyaneruDAOImpl
    dao.create(ayaneru)
  }
}

