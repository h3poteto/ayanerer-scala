package actors

import akka.actor._
import play.api.Logger

trait ApplicationActor extends Actor {
  def receive = {
    case message: String => {
      Logger.info(message)
      execute()
    }
  }

  def execute()
}
