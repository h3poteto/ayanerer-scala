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
    ayaneruDao.insert(ayaneru).map { _ =>
      val uploadActor = system.actorOf(Props[ImageUploadActor])
      system.scheduler.scheduleOnce(1 seconds, uploadActor, "save")
      Redirect(routes.HomeController.index)
    }
  }
}

