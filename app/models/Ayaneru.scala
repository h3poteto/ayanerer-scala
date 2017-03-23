package models

import spray.json._
import DefaultJsonProtocol._

object AyaneruJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Ayaneru.apply)
}

case class Ayaneru(id: Option[Int] = None, image: String)
