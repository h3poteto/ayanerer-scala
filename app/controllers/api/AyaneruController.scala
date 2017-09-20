package controllers.api

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import spray.json._
import dao.AyaneruDAO
import models.Ayaneru
import models.AyaneruJsonProtocol._

@Singleton
class AyaneruController @Inject() (components: ControllerComponents,ayaneruDao: AyaneruDAO) extends AbstractController(components) {
  def index = Action {
    val ayanerus = ayaneruDao.all()
    println(ayanerus)
    Ok(ayanerus.toJson.prettyPrint)
  }
}

