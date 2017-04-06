package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, SeedSearchRequest }
import akka.actor._

class SeedImageTask extends Task {
  def task(app: Application) = {
    getImages(app, "佐倉綾音")
  }

  def getImages(app: Application, name: String) = {
    // limit: 10までしか受け付けてくれないので繰り返しをやるしかない
    val limit = 10
    for(offset <- (0 to 9).toList.map { i => i * limit + 1 }) {
      val request = new SeedSearchRequest(name, limit, offset)
      val injector = app.injector
      val actorSystem = injector.instanceOf[ActorSystem]
      val downloader = new GoogleSearchResultDownloader(request, actorSystem)
      downloader.download()
    }
  }
}
