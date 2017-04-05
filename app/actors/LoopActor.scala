package actors

import akka.actor._
import play.api.Logger

object LoopActor {
  case class Loop(n: Int)
  case class Ping()
}

class LoopActor extends Actor {
  import LoopActor._

  def receive = {
    case Ping() =>
      sender ! "pong"
    case Loop(n) =>
      Logger.info("message received")
      execute()
  }

  def execute() = {
    for(i <- 0 to 9) {
      Thread.sleep(1000)
      Logger.info("loop")
    }
    Logger.info("done")
  }

  override def preStart = {
    Logger.info("loop actor started")
  }

  override def postStop = {
    Logger.info("loop actor stopped")
  }
}
