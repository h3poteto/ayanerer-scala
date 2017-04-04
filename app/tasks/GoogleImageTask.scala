package tasks

import javax.inject._
import play.api._
import models.{ GoogleSearchResultDownloader, GoogleSearchRequest }
import akka.actor._

class GoogleImageTask extends Task {
  def task(app: Application) = {
    getImages(app, "佐倉綾音")
  }

  def getImages(app: Application, name: String) = {
    val request = new GoogleSearchRequest(name)
    val injector = app.injector
    val actorSystem = injector.instanceOf[ActorSystem]
    val downloader = new GoogleSearchResultDownloader(request, actorSystem)
    downloader.download()
  }
}
