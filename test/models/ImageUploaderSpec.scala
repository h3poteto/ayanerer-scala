import org.scalatestplus.play._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import models.ImageUploader

class ImageUploaderSpec extends PlaySpec {
  "extension" when {
    "is not image" should {
      val url =  "https://iwiz-chie.c.yimg.jp/im_siggLndOLqvkxKrxaGzKQJh.mA---x320-y320-exp5m-n1/d/iwiz-chie/que-11160539271"
      "return none" in {
        val uploader = new ImageUploader(url)
        uploader.extension mustBe None
      }
    }

    "is image" should {
      val url = "http://dengekionline.com/elem/000/000/854/854050/gfkari_01_cs1w1_590x.jpg"
      "return extension" in {
        val uploader = new ImageUploader(url)
        uploader.extension mustBe Some("jpg")
      }
    }
  }
}
