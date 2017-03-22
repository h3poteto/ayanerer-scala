package tasks

import java.net.URL
import dispatch._, Defaults._
import spray.json._
import GoogleSearchResponseProtocol._

class GoogleImageTask extends Task {
  def run {
    getImages("佐倉綾音")
  }

  private def getImages(name: String) {
    val response = searchImages(name)
    response.items.map(saveImage(_))
  }

  private def saveImage(item: GoogleSearchItem) {
    val url = storeImage(item.link)
  }

  // urlに指定された画像を/tmpにダウンロードしs3にアップロードしてurlを返す
  private def storeImage(url: String): String = {
    println(url)
    ""
  }

  private def searchImages(name: String): GoogleSearchResponse = {
    val svc = url(googleSearchRequest(name))
    val future = Http(svc OK as.String)
    val f = future()
    f.parseJson.convertTo[GoogleSearchResponse]
  }

  private def googleSearchRequest(name: String): String = {
    s"https://www.googleapis.com/customsearch/v1?key=$googleApiKey&cx=$googleCustomSearchId&searchType=image&q=" + keyword(name) + "&num=10"
  }

  private def googleCustomSearchId: String = {
    sys.env.getOrElse("GOOGLE_CUSTOM_SEARCH_ID", "")
  }

  private def googleApiKey: String = {
    sys.env.getOrElse("GOOGLE_API_KEY", "")
  }

  private def keyword(name: String): String = {
    java.net.URLEncoder.encode(name, "UTF-8")
  }
}
