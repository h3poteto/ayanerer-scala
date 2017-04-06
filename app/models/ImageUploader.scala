package models

import java.security.MessageDigest
import java.io.File
import dispatch._
import scala.concurrent.ExecutionContext.Implicits.global

class ImageUploader(val url: String) {
  // http://stackoverflow.com/questions/5564074/scala-http-operations
  // Headerを付けないと403を返す画像が混ざっている
  def download(): Option[String] = {
    extension match {
      case Some(ext) => {
        val fileName: String = s"/tmp/$sha1.$ext"
        val request = dispatch.url(url)
        dispatch.Http(request <:< Map("User-Agent" -> "Mozilla/5.0") > dispatch.as.File(new File(fileName)))

        Some(fileName)
      }
      case _ => None
    }
  }

  def sha1: String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(url.getBytes)
    md.digest.foldLeft("") { (s, b) => s + "%02x".format(if(b < 0) b + 256 else b) }
  }

  def extension: Option[String] = {
    val pattern = "(.*)(?:\\.([^.]+$))".r
    url match {
      case pattern(k, v) if image(v) => {
        Some(v)
      }
      case _ => None
    }
  }

  private def image(ext: String): Boolean = {
    ext match {
      case "png" | "jpeg" | "jpg" | "gif" => {
        true
      }
      case _ => false
    }
  }
}
