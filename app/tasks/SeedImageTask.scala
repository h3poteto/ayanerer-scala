package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, SeedSearchRequest }
import akka.actor._

class SeedImageTask extends Task {
  def task(app: Application) = {
    getImages(app, "佐倉綾音")
  }

  // TODO: limit: 10までしか指定できないため繰り返ししないといけない
  def getImages(app: Application, name: String) = {
    val request = new SeedSearchRequest(name, 10)
    val injector = app.injector
    val actorSystem = injector.instanceOf[ActorSystem]
    val downloader = new GoogleSearchResultDownloader(request, actorSystem)
    downloader.download()
  }
}
