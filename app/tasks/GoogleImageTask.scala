package tasks

import java.net.URL
import dispatch._, Defaults._
import spray.json._
import GoogleSearchResponseProtocol._
import dao.AyaneruDAO
import models.Ayaneru
import play.api._

class GoogleImageTask extends Task {
  // TODO: Modeの入れ替え
  val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
  val context = ApplicationLoader.createContext(env)
  val loader = ApplicationLoader(context)
  val app = loader.load(context)

  def run() {
    try {
      getImages("佐倉綾音")
    } finally {
      Play.stop(app)
    }
  }

  private def getImages(name: String) {
    val response = searchImages(name)
    response.items.map(saveImage(_))
  }

  private def saveImage(item: GoogleSearchItem) {
    val url = storeImage(item.link)
    val app2dao = Application.instanceCache[AyaneruDAO]
    val dao = app2dao(app)
    val ayaneru = new Ayaneru(None, url)
    dao.insert(ayaneru)
  }

  // urlに指定された画像を/tmpにダウンロードしs3にアップロードしてurlを返す
  private def storeImage(url: String): String = {
    url
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
