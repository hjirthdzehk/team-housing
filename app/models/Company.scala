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
    sql"""select ${c.result.*} from ${Company as c}
          where ${c.id} = ${id} and ${c.deletedAt} is null
       """.map(Company(c)).single().apply()

  def findAll()(implicit session: DBSession = autoSession): List[Company] = {
    val query =
      sql"""SELECT ${c.result.*} FROM ${Company as c}
          WHERE ${c.deletedAt} IS NULL
          ORDER BY ${c.id}
       """
    query.map(Company(c)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long =
    sql"""select count(*) from ${Company as c}
          where ${c.deletedAt} is null
       """.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Company] =
    sql"""select ${c.result.*} from ${Company as c}
          where ${c.deletedAt} is null and ${where}
          order by ${c.id}
      """.map(Company(c)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long =
    sql"""select count(*) from ${Company as c}
         where ${c.deletedAt} is null and ${where}
       """.map(_.long(1)).single.apply().get

  def create(
    name: String,
    url: Option[String] = None,
    createdAt: DateTime = DateTime.now
  )(implicit session: DBSession = autoSession): Company = {
    val id = sql"""insert into ${table} (${column.name}, ${column.url}, ${column.createdAt})
                   values (${name}, ${url}, ${createdAt})"""
      .updateAndReturnGeneratedKey.apply()

    Company(id = id, name = name, url = url, createdAt = createdAt)
  }

  def save(m: Company)(implicit session: DBSession = autoSession): Company = {
    sql"""update ${table}
          set ${column.name} = ${m.name}, ${column.url} = ${m.url}
          where ${column.id} = ${m.id} and ${column.deletedAt} is null
       """.update.apply()
    m
  }

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit =
    sql"""update ${table}
          set ${column.deletedAt} = ${DateTime.now}
          where ${column.id} = ${id}
       """.update.apply()
}
