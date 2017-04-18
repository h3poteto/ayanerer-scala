import org.scalatestplus.play._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._
import play.api.test._
import play.api.test.Helpers._
import play.api.inject._
import play.api.inject.guice._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import dao.AyaneruDAO
import models.Ayaneru


class AyaneruControllerSpec extends PlaySpec with OneAppPerTest  with ScalaFutures with MockitoSugar {
  "index" when {
    "Empty object" should {
      // http://www.innovaedge.com/2015/07/01/how-to-use-mocks-in-injected-objects-with-guiceplayscala/
      val ayaneruDaoMock = mock[AyaneruDAO]
      when(ayaneruDaoMock.all()).thenReturn(Seq.empty[Ayaneru])

      val mockApp = new GuiceApplicationBuilder().
        overrides(bind[AyaneruDAO].toInstance(ayaneruDaoMock)).
        build

      "success" in {
        val index = route(mockApp, FakeRequest(GET, "/api/ayanerus")).get

        status(index) mustBe OK
        contentAsString(index) mustBe "[]"
      }
    }

    "Some object" should {
      val ayaneruDaoMock = mock[AyaneruDAO]
      when(ayaneruDaoMock.all()).thenReturn(Seq(new Ayaneru(Some(1), None, "sample")))

      val mockApp = new GuiceApplicationBuilder().
        overrides(bind[AyaneruDAO].toInstance(ayaneruDaoMock)).
        build

      "success" in {
        val index = route(mockApp, FakeRequest(GET, "/api/ayanerus")).get

        contentAsString(index) mustBe """[{
  "id": 1,
  "originalURL": "sample"
}]"""
      }
    }
  }
}
