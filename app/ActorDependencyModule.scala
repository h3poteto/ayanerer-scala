import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import actors.ImageUploadActor

class ActorDependencyModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[ImageUploadActor]("image-upload-actor")
  }
}
