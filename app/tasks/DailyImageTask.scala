package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, DailySearchRequest }
import akka.actor._

// How to run this task:
// in development:
// $ activator "run-main tasks.DailyImageTask"
// in production:
// $ cd ayanerer-1.0-SNAPSHOT
// $ bin/ayanerer -main tasks.DailyImageTask -Dconfig.file=./conf/prod.conf


object DailyImageTask extends App with Task {
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
