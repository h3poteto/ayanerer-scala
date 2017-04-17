package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, DailySearchRequest }
import akka.actor._
import actors.ImageUploadActor
import dao.AyaneruDAO
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

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
    val dao = injector.instanceOf[AyaneruDAO]
    val actor = actorSystem.actorOf(Props(classOf[ImageUploadActor], dao), "imageUploader")
    val downloader = new GoogleSearchResultDownloader(request, actor, dao)
    val result: List[Future[Boolean]] = downloader.download()
    val f: Future[List[Boolean]] = Future.sequence(result)
    f.onSuccess {
      case r: List[Boolean] => r.map {println(_)}
    }
    Await.ready(f, Duration.Inf)
  }
}
