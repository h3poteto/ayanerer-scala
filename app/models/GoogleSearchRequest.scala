package models

import dispatch._, Defaults._
import javax.inject.Inject
import java.util.Calendar

trait GoogleSearchRequest {

  def request(): Future[String] = {
    val svc = url(googleSearchRequest)
    Http(svc OK as.String)
  }

  def googleSearchRequest: String

  protected def googleCustomSearchId: String = {
    sys.env.getOrElse("GOOGLE_CUSTOM_SEARCH_ID", "")
  }

  protected def googleApiKey: String = {
    sys.env.getOrElse("GOOGLE_API_KEY", "")
  }

  protected def keyword(name: String): String = {
    java.net.URLEncoder.encode(name, "UTF-8")
  }
}

class SeedSearchRequest @Inject()(val name: String, val limit: Int, val offset: Int) extends GoogleSearchRequest {
  def googleSearchRequest: String = {
    s"https://www.googleapis.com/customsearch/v1?key=$googleApiKey&cx=$googleCustomSearchId&searchType=image&q=" + keyword(name) + "&num=" + limit.toString() + s"&start=$offset"
  }
}

class DailySearchRequest @Inject() (val name: String, val limit: Int) extends GoogleSearchRequest {
  // sortについて: https://developers.google.com/custom-search/docs/structured_search#sort_by_attribute
  //
  def googleSearchRequest: String = {
    s"https://www.googleapis.com/customsearch/v1?key=$googleApiKey&cx=$googleCustomSearchId&searchType=image&q=" + keyword(name) + "&num=" + limit.toString() + s"&sort=date-sdate:r:$startDate:$endDate"
  }

  private def startDate: String = {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -1)
    "%tY%<tm%<td".format(calendar.getTime)
  }

  private def endDate: String = {
    "%tY%<tm%<td".format(Calendar.getInstance().getTime())
  }
}
