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
class AyaneruController @Inject() (ayaneruDao: AyaneruDAO, val messagesApi: MessagesApi, system: ActorSystem, @Named("image-upload-actor") imageUploadActor: ActorRef) extends Controller with I18nSupport {
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
      imageUploadActor ! ImageUploadActor.Upload(id)
      Redirect(routes.HomeController.index)
    }
  }
}

