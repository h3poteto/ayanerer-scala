package tasks

import play.api._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import actors.LoopActor
import scala.concurrent.{Await, Future}
import scala.util.{Success, Failure}

class LoopTask  extends Task {

  def task(app: Application) = {
    loop(app)
  }

  def loop(app: Application) = {
    val injector = app.injector
    val actorSystem = injector.instanceOf[ActorSystem]
    val actor = actorSystem.actorOf(Props[LoopActor])
    Runtime.getRuntime.addShutdownHook(stop(actor))
    actor ! LoopActor.Loop(1)
  }

  def stop(actor: ActorRef) = {
    new Thread {
      override def run() = {
        implicit val timeout = Timeout(30 seconds)
        val f:Future[String] = (actor ? LoopActor.Ping()).mapTo[String]
        Await.ready(f, Duration.Inf)
        f.value.get match {
          case Success(r) => Logger.info(s"Success to stop: $r")
          case Failure(r) => Logger.error(s"Failed to stop: $r")
        }
      }
    }
  }

}

