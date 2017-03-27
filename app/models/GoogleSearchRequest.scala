package models

import java.net.URL
import dispatch._, Defaults._
import javax.inject.Inject

class GoogleSearchRequest @Inject()(val name: String) {
  def request(): Future[String] = {
    val svc = url(googleSearchRequest)
    Http(svc OK as.String)
  }

  private def googleSearchRequest: String = {
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
