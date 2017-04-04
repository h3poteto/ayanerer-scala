package models

import spray.json._

object GoogleSearchResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val googleSearchImageFormat = jsonFormat7(GoogleSearchImage.apply)
  implicit val googleSearchItemFormat = jsonFormat9(GoogleSearchItem.apply)
  implicit val googleSearchJsonResponseFormat = jsonFormat1(GoogleSearchResponse.apply)
}

case class GoogleSearchResponse(items: Option[List[GoogleSearchItem]])

case class GoogleSearchItem(kind: String, title: String, htmlTitle: String, link: String, displayLink: String, snippet: String, htmlSnippet: String, mime: String, image: GoogleSearchImage)

case class GoogleSearchImage(contextLink: String, height: Int, width: Int, byteSize: Int, thumbnailLink: String, thumbnailHeight: Int, thumbnailWidth: Int)
