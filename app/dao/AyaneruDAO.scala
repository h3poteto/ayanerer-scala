package dao

import scalikejdbc._
import skinny.orm._
import org.joda.time._
import models.Ayaneru

trait AyaneruDAO {
  def create(ayaneru: Ayaneru): Long
  def findById(id: Int): Option[Ayaneru]
  def all(): Seq[Ayaneru]
}

class AyaneruDAOImpl extends SkinnyCRUDMapper[Ayaneru] with AyaneruDAO {
  override lazy val defaultAlias = createAlias("a")
  override lazy val tableName = "ayanerus"
  private[this] lazy val a = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[Ayaneru]): Ayaneru = Ayaneru(
    id = Some(rs.int(rn.id)),
    image = rs.string(rn.image)
  )

  def create(ayaneru: Ayaneru): Long = createWithNamedValues(
    column.image -> ayaneru.image
  )

  def findById(id: Int): Option[Ayaneru] = where(sqls.eq(a.id, id)).apply().headOption

  def all(): Seq[Ayaneru] = findAll()
}
