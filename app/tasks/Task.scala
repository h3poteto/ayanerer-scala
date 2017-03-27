package tasks

import play.api._

trait Task extends Runnable {
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
    sys.env("PLAY_ENV") match {
      case "development" => Mode.Dev
      case "production" => Mode.Prod
      case "test" => Mode.Test
    }
  }

  def application(): Application = {
    val env = Environment(new java.io.File("."), this.getClass.getClassLoader, environment)
    val context = ApplicationLoader.createContext(env)
    val loader = ApplicationLoader(context)
    loader.load(context)
  }
}
