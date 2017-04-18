import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import actors._

class ActorModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[ImageUploadActor]("imageUploadActor")
  }
}
