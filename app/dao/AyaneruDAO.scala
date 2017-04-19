package dao

import scalikejdbc._, jsr310._
import skinny.orm._
import skinny.orm.feature.TimestampsFeature
import models.Ayaneru
import java.sql.SQLException
import play.api.Logger
import java.time.ZonedDateTime

trait AyaneruDAO {
  def create(ayaneru: Ayaneru): Option[Long]
  def findById(id: Int): Option[Ayaneru]
  def all(): Seq[Ayaneru]
  def update(ayaneru: Ayaneru): Unit
}

class AyaneruDAOImpl extends SkinnyCRUDMapper[Ayaneru] with AyaneruDAO with TimestampsFeature[Ayaneru] {
  override lazy val defaultAlias = createAlias("a")
  override lazy val tableName = "ayanerus"
  private[this] lazy val a = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[Ayaneru]): Ayaneru = Ayaneru(
    id = Option(rs.int(rn.id)),
    originalURL = rs.string(rn.originalURL),
    imageURL = Option(rs.string(rn.imageURL)),
    createdAt = Option(rs.get[ZonedDateTime](rn.createdAt)),
    updatedAt = Option(rs.get[ZonedDateTime](rn.updatedAt))
  )

  def create(ayaneru: Ayaneru): Option[Long] = {
    try {
      Some(
        createWithNamedValues(
          column.originalURL -> ayaneru.originalURL,
          column.imageURL -> ayaneru.imageURL
        )
      )
    } catch {
      // Duplicate Entryだけは拾ってあげる
      case e: SQLException if e.getErrorCode == 1062 => {
        Logger.warn(e.getLocalizedMessage)
        None
      }
    }
  }

  def findById(id: Int): Option[Ayaneru] = where(sqls.eq(a.id, id)).apply().headOption

  def all(): Seq[Ayaneru] = findAll()

  def update(ayaneru: Ayaneru): Unit = {
    for (id <- ayaneru.id) yield {
      updateById(id.toLong).withAttributes('original_url -> ayaneru.originalURL, 'image_url -> ayaneru.imageURL)
    }
  }
}
