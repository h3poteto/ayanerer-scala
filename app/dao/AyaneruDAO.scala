package dao

import scala.concurrent.Future
import javax.inject.Inject
import models.Ayaneru
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

class AyaneruDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Ayanerus = TableQuery[AyanerusTable]

  def findById(id: Int): Future[Option[Ayaneru]] = db.run(Ayanerus.filter(_.id === id).result.headOption)

  def all(): Future[Seq[Ayaneru]] = db.run(Ayanerus.result)

  def insert(ayaneru: Ayaneru): Future[Int] = db.run(Ayanerus += ayaneru)

  def update(ayaneru: Ayaneru): Future[Int] = db.run(Ayanerus.filter(_.id === ayaneru.id).update(ayaneru)).map { id => id }

  private class AyanerusTable(tag: Tag) extends Table[Ayaneru](tag, "ayanerus") {
    def id = column[Int]("id", O.PrimaryKey)
    def image = column[String]("image")

    def * = (id.?, image) <> (Ayaneru.tupled, Ayaneru.unapply _)
  }
}
