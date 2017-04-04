package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, DailySearchRequest }
import akka.actor._

class DailyImageTask extends Task {
  def task(app: Application) = {
    getImages(app, "佐倉綾音")
  }

  // limit: 10が上限だが1日の増加量はそれほど多くないと期待して10件まであれば十分
  def getImages(app: Application, name: String) = {
    val request = new DailySearchRequest(name, 10)
    val injector = app.injector
    val actorSystem = injector.instanceOf[ActorSystem]
    val downloader = new GoogleSearchResultDownloader(request, actorSystem)
    downloader.download()
  }
}
