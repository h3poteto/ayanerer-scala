package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import dao.AyaneruDAO
import models.Ayaneru


@Singleton
class AyaneruController @Inject() (ayaneruDao: AyaneruDAO) extends Controller {
  def index = Action.async {
    ayaneruDao.all().map {
      ayanerus => Ok(Json.obj("status" -> "OK"))
    }
  }
}
