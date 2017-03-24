import org.scalatestplus.play._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import play.api._
import play.api.test._
import play.api.test.Helpers._

class AyaneruControllerSpec extends PlaySpec with OneAppPerTest  with ScalaFutures {

  "index" should {
    "Empty object" in {
      val index = route(app, FakeRequest(GET, "/api/ayanerus")).get

      status(index) mustBe OK
    }
  }
}
