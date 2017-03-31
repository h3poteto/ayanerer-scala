package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, GoogleSearchRequest }

class GoogleImageTask extends Task {
  def task(app: Application) = {
    getImages("佐倉綾音")
  }

  def getImages(name: String) = {
    val request = new GoogleSearchRequest(name)
    val downloader = new GoogleSearchResultDownloader(request)
    downloader.download()
  }
}
