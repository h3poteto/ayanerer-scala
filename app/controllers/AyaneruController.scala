package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.i18n.{MessagesApi, Messages, I18nSupport}
import dao.AyaneruDAO
import models.Ayaneru
import actors.ImageUploadActor
import akka.actor._

@Singleton
class AyaneruController @Inject() (val messagesApi: MessagesApi, system: ActorSystem, ayaneruDao: AyaneruDAO) extends Controller with I18nSupport {
  val registrationForm = Form[Ayaneru](
    mapping(
      "id"    -> ignored[Option[Int]](None),
      "image" -> text
    )(Ayaneru.apply)(Ayaneru.unapply)
  )

  def registration = Action {
    Ok(views.html.ayaneru.registration(registrationForm))
  }

  def create = Action { implicit request =>
    val ayaneru: Ayaneru = registrationForm.bindFromRequest.get
    val id = ayaneruDao.create(ayaneru)
    id match {
      case Some(id) => {
        val actor = system.actorOf(Props[ImageUploadActor])
        actor ! ImageUploadActor.Upload(id.toInt, ayaneruDao)
      }
      case None => {
      }
    }
    Redirect(routes.HomeController.index)
  }
}

