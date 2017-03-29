package actors

import play.api.Logger

class ImageUploadActor extends ApplicationActor {
  def execute() {
    Logger.info("called")
  }
}
