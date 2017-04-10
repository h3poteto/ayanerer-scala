package tasks

import play.api._

trait Task extends Runnable {
  run()

  final def run() {
    val app = application()
    try {
      task(app)
    } finally {
      Play.stop(app)
    }
  }

  def task(app: Application)

  def environment(): Mode.Mode = {
    System.getProperty("play.mode") match {
      case "Prod" => Mode.Prod
      case _ => Mode.Dev
    }
  }

  def application(): Application = {
    val env = Environment(new java.io.File("."), this.getClass.getClassLoader, environment)
    val context = ApplicationLoader.createContext(env)
    val loader = ApplicationLoader(context)
    loader.load(context)
  }
}
