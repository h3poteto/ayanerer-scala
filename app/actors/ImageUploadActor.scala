package actors

import play.api.Logger
import akka.actor._
import dao.AyaneruBase
import spray.json._
import models.AyaneruJsonProtocol._
import models.Ayaneru
import scala.concurrent.ExecutionContext.Implicits.global
import sys.process._
import java.net.URL
import java.io.File
import java.security.MessageDigest
import scala.concurrent.Future

object ImageUploadActor {
  case class Upload(id: Int, dao: AyaneruBase)
}

class ImageUploadActor extends Actor {
  import ImageUploadActor._

  def receive = {
    case Upload(id, dao) =>
      execute(id, dao)
  }

  def execute(id: Int, dao: AyaneruBase):Boolean = {
    val ayaneru = dao.findById(id)
    Logger.info(ayaneru.toJson.prettyPrint)
    ayaneru match {
      case Some(Ayaneru(_,_)) => {
        val aya = ayaneru.get
        println(download(aya.image))
        true
      }
      case None => false
    }
  }

  def download(url: String): String = {
    val fileName: String = "/tmp/" + sha1(url) + "." + extension(url)
    new URL(url) #> new File(fileName) !!

    fileName
  }

  def sha1(str: String): String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(str.getBytes)
    md.digest.foldLeft("") { (s, b) => s + "%02x".format(if(b < 0) b + 256 else b) }
  }

  def extension(url: String): String = {
    val pattern = "(.*)(?:\\.([^.]+$))".r
    url match {
      case pattern(k, v) => {
        v
      }
      case _ => ""
    }
  }
}
