package tasks

import play.api._
import dao.AyaneruDAO
import models.{ GoogleSearchResultDownloader, GoogleSearchRequest }

class GoogleImageTask extends Task {
  def task(app: Application) {
    getImages(dao(app), "佐倉綾音")
  }

  def getImages(dao: AyaneruDAO, name: String) {
    val request = new GoogleSearchRequest(name)
    val downloader = new GoogleSearchResultDownloader(dao, request)
    downloader.download()
  }

  private def dao(app: Application): AyaneruDAO = {
    val app2dao = Application.instanceCache[AyaneruDAO]
    app2dao(app)
  }
}
