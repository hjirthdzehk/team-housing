package models

import scalikejdbc._
import org.joda.time.DateTime

case class Company(
    id: Long,
    name: String,
    url: Option[String] = None,
    createdAt: DateTime,
    deletedAt: Option[DateTime] = None
) {

  def save()(implicit session: DBSession = Company.autoSession): Company = {
    Company.save(this)(session)
  }
  def destroy()(implicit session: DBSession = Company.autoSession): Unit = {
    Company.destroy(id)(session)
  }
}

object Company extends SQLSyntaxSupport[Company] {

  def apply(c: SyntaxProvider[Company])(rs: WrappedResultSet): Company = {
    apply(c.resultName)(rs)
  }
  def apply(c: ResultName[Company])(rs: WrappedResultSet): Company = new Company(
    id = rs.get(c.id),
    name = rs.get(c.name),
    url = rs.get(c.url),
    createdAt = rs.get(c.createdAt),
    deletedAt = rs.get(c.deletedAt)
  )

  val c = Company.syntax("c")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Company] =
    sql"""select * from ${tableName}
          where id = ${id} and deleted_at is not null
       """.map(Company(c)).single().apply()

  def findAll()(implicit session: DBSession = autoSession): List[Company] =

    sql"""select * from ${tableName}
          where deleted_at is not null
          order by id
       """.map(Company(c)).list.apply()

  def countAll()(implicit session: DBSession = autoSession): Long =
    sql"""select count(*) from ${tableName}
          where deleted_at is not null
       """.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Company] =
    sql"""select * from ${tableName}
          where deleted_at is not null and ${where}
          order by id
      """.map(Company(c)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long =
    sql"""select count(*) from ${tableName}
         where deleted_at is not null and ${where}
       """.map(_.long(1)).single.apply().get

  def create(
    name: String,
    url: Option[String] = None,
    createdAt: DateTime = DateTime.now
  )(implicit session: DBSession = autoSession): Company = {

    val id = sql"""insert into ${tableName} (name, url, created_at)
                   values (${name}, ${url}, ${createdAt})"""
      .updateAndReturnGeneratedKey.apply()

    Company(id = id, name = name, url = url, createdAt = createdAt)
  }

  def save(m: Company)(implicit session: DBSession = autoSession): Company = {
    sql"""update ${tableName}
          set name = ${m.name}, url = ${m.url}
          where id = ${m.id} and deleted_at is null
       """.update.apply()
    m
  }

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit =
    sql"""update ${tableName}
          set deleted_at = ${DateTime.now}
          where id = ${id}
       """.update.apply()
}
