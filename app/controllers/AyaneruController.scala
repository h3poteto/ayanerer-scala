package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{MessagesApi, Messages, I18nSupport}
import dao.AyaneruDAO
import models.Ayaneru
import actors.ImageUploadActor
import akka.actor._
import scala.concurrent.duration._

@Singleton
class AyaneruController @Inject() (ayaneruDao: AyaneruDAO, val messagesApi: MessagesApi, system: ActorSystem) extends Controller with I18nSupport {
  val registrationForm = Form[Ayaneru](
    mapping(
      "id"    -> ignored[Option[Int]](None),
      "image" -> text
    )(Ayaneru.apply)(Ayaneru.unapply)
  )

  def registration = Action {
    Ok(views.html.ayaneru.registration(registrationForm))
  }

  def create = Action.async { implicit request =>
    val ayaneru: Ayaneru = registrationForm.bindFromRequest.get
    ayaneruDao.insert(ayaneru).map { id =>
      // TODO: このidはauto incrementの値ではないので要修正
      val actor = system.actorOf(Props[ImageUploadActor])
      actor ! ImageUploadActor.Upload(id, ayaneruDao)
      Redirect(routes.HomeController.index)
    }
  }
}

