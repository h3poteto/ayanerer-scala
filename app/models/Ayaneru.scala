package models

import java.time.ZonedDateTime
import spray.json._
import utils.DateUtils.DateJsonFormat

object AyaneruJsonProtocol extends DefaultJsonProtocol{
  implicit val format = jsonFormat5(Ayaneru.apply)
}

case class Ayaneru(id: Option[Int] = None, imageURL: Option[String] = None, originalURL: String, createdAt: Option[ZonedDateTime] = None, updatedAt: Option[ZonedDateTime] = None)
