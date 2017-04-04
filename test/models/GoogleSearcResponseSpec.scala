import org.scalatestplus.play._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import spray.json._
import models.GoogleSearchResponseJsonProtocol._
import models.{ GoogleSearchResponse, GoogleSearchItem, GoogleSearchImage }

class GoogleSearchResponseSpec extends PlaySpec {
  "parse" should {
    "success" in {
      val jsonResponse = """
{

    "kind": "customsearch#search",
    "url": {
        "type": "application/json",
        "template": "https://www.googleapis.com/customsearch/v1?q={searchTerms}&num={count?}&start={startIndex?}&lr={language?}&safe={safe?}&cx={cx?}&cref={cref?}&sort={sort?}&filter={filter?}&gl={gl?}&cr={cr?}&googlehost={googleHost?}&c2coff={disableCnTwTranslation?}&hq={hq?}&hl={hl?}&siteSearch={siteSearch?}&siteSearchFilter={siteSearchFilter?}&exactTerms={exactTerms?}&excludeTerms={excludeTerms?}&linkSite={linkSite?}&orTerms={orTerms?}&relatedSite={relatedSite?}&dateRestrict={dateRestrict?}&lowRange={lowRange?}&highRange={highRange?}&searchType={searchType}&fileType={fileType?}&rights={rights?}&imgSize={imgSize?}&imgType={imgType?}&imgColorType={imgColorType?}&imgDominantColor={imgDominantColor?}&alt=json"
    },
    "queries": {
        "request": [
            {
                "title": "Google Custom Search - 佐倉綾音",
                "totalResults": "1320000",
                "searchTerms": "佐倉綾音",
                "count": 10,
                "startIndex": 1,
                "inputEncoding": "utf8",
                "outputEncoding": "utf8",
                "safe": "off",
                "cx": "000136758670674945236:favbsqfd6_0",
                "searchType": "image"
            }
        ],
        "nextPage": [
            {
                "title": "Google Custom Search - 佐倉綾音",
                "totalResults": "1320000",
                "searchTerms": "佐倉綾音",
                "count": 10,
                "startIndex": 11,
                "inputEncoding": "utf8",
                "outputEncoding": "utf8",
                "safe": "off",
                "cx": "000136758670674945236:favbsqfd6_0",
                "searchType": "image"
            }
        ]
    },
    "context": {
        "title": "Google"
    },
    "searchInformation": {
        "searchTime": 0.626844,
        "formattedSearchTime": "0.63",
        "totalResults": "1320000",
        "formattedTotalResults": "1,320,000"
    },
    "items": [
        {
            "kind": "customsearch#result",
            "title": "佐倉綾音」の検索結果 - Yahoo!検索（画像）",
            "htmlTitle": "<b>佐倉綾音</b>」の検索結果 - Yahoo!検索（画像）",
            "link": "http://static.pinky-media.jp/matome/file/parts/I0007005/3ae2afeb181e3cd8174757b6068c4e43.jpg",
            "displayLink": "search.yahoo.co.jp",
            "snippet": "佐倉綾音の胸の.",
            "htmlSnippet": "<b>佐倉綾音</b>の胸の.",
            "mime": "image/jpeg",
            "image": {
                "contextLink": "https://search.yahoo.co.jp/image/search?p=%E4%BD%90%E5%80%89%E7%B6%BE%E9%9F%B3&ei=UTF-8",
                "height": 1200,
                "width": 917,
                "byteSize": 879123,
                "thumbnailLink": "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcReLt8V3A4T6ULWsr2wOJlaFfIScr00hzTmCyd0t6Rdy9qFDjCgqcgBhIel",
                "thumbnailHeight": 150,
                "thumbnailWidth": 115
            }
        }
    ]
}
"""
      val items = jsonResponse.parseJson.convertTo[GoogleSearchResponse].items
      items.get.length mustBe 1
      items.get.map { item =>
        item.image.isInstanceOf[GoogleSearchImage]
      }
    }
  }
}
