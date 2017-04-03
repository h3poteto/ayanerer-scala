import com.google.inject.AbstractModule
import dao._

class DAOModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AyaneruDAO]).to(classOf[AyaneruDAOImpl])
  }
}
