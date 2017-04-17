package tasks

import play.api._
import models.{ GoogleSearchResultDownloader, SeedSearchRequest }
import akka.actor._
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import actors.ImageUploadActor
import dao.AyaneruDAO

object SeedImageTask extends App with Task {
  def task(app: Application) = {
    getImages(app, "佐倉綾音")
  }

  def getImages(app: Application, name: String) = {
    // limit: 10までしか受け付けてくれないので繰り返しをやるしかない
    val limit = 10
    // TODO: 補番稼働時には一度に100件程度取れるようにしたい
    for(offset <- (0 to 0).toList.map { i => i * limit + 1 }) {
      val request = new SeedSearchRequest(name, limit, offset)
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
}
