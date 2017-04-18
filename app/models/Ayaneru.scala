package models

import spray.json._
import DefaultJsonProtocol._

object AyaneruJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat3(Ayaneru.apply)
}

case class Ayaneru(id: Option[Int] = None, imageURL: Option[String] = None, originalURL: String)
