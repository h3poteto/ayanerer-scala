package controllers

import javax.inject.{Inject, Named, Singleton}
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.i18n.{MessagesApi, Messages, I18nSupport}
import dao.AyaneruDAO
import models.Ayaneru
import actors.events.ImageUploadEvent
import akka.actor._
import java.time.ZonedDateTime

@Singleton
class AyaneruController @Inject() (val messagesApi: MessagesApi, @Named("imageUploadActor") imageUploadActor: ActorRef, ayaneruDao: AyaneruDAO) extends Controller with I18nSupport {
  val registrationForm = Form[Ayaneru](
    mapping(
      "id"    -> ignored[Option[Int]](None),
      "imageURL" -> ignored[Option[String]](None),
      "originalURL" -> text,
      "createdAt" -> ignored[Option[ZonedDateTime]](None),
      "updatedAt" -> ignored[Option[ZonedDateTime]](None)
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
        imageUploadActor ! ImageUploadEvent.Upload(id.toInt)
      }
      case None => {
      }
    }
    Redirect(routes.HomeController.index)
  }
}

