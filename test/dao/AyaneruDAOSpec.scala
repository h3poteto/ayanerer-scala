import com.h3poteto.ayanerer.test._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import play.api._
import play.api.test._
import play.api.test.Helpers._
import models.Ayaneru
import dao.AyaneruDAO

class AyaneruDAOSpec extends DatabaseSpec with ScalaFutures {
  def ayaneruDao(implicit app: Application): AyaneruDAO = Application.instanceCache[AyaneruDAO].apply(app)

  "insert" should {
    "success" in {
      val ayaneru = new Ayaneru(None, "sample")
      val result = ayaneruDao.create(ayaneru)
      result mustBe(_: Long)
    }
  }

  "all" should {
    "success" in {
      val ayaneru = new Ayaneru(None, "sample")
      ayaneruDao.create(ayaneru)
      ayaneruDao.all()(0).image mustBe ayaneru.image
    }
  }
}
