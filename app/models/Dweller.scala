package models

import scalikejdbc._

case class Dweller(personId: Int) {

  def getPerson: Person = Person.find(personId).get

  def listFlats: Seq[Flat] = Flat.listByPersonId(personId)

  def save()(implicit session: DBSession = Dweller.autoSession): Dweller = Dweller.save(this)(session)

  def destroy()(implicit session: DBSession = Dweller.autoSession): Unit = Dweller.destroy(this)(session)

}


object Dweller extends SQLSyntaxSupport[Dweller] {

  override val tableName = "dweller"

  override val columns = Seq("person_id")

  def apply(d: SyntaxProvider[Dweller])(rs: WrappedResultSet): Dweller = apply(d.resultName)(rs)
  def apply(d: ResultName[Dweller])(rs: WrappedResultSet): Dweller = new Dweller(
    personId = rs.get(d.personId)
  )

  val d = Dweller.syntax("d")

  override val autoSession = AutoSession

  def find(personId: Int)(implicit session: DBSession = autoSession): Option[Dweller] = {
    sql"""select ${d.result.*} from ${Dweller as d} where ${d.personId} = ${personId}"""
      .map(Dweller(d.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Dweller] = {
    sql"""select ${d.result.*} from ${Dweller as d}""".map(Dweller(d.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Dweller.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Dweller] = {
    sql"""select ${d.result.*} from ${Dweller as d} where ${where}"""
      .map(Dweller(d.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Dweller] = {
    sql"""select ${d.result.*} from ${Dweller as d} where ${where}"""
      .map(Dweller(d.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Dweller as d} where ${where}"""
      .map(_.long(1)).single.apply().get
  }

  def create(personId: Int)(implicit session: DBSession = autoSession): Dweller = {
    sql"""insert into ${Dweller.table} (${column.personId})
          values (${personId})""".updateAndReturnGeneratedKey.apply()

    Dweller(personId = personId)
  }

  def batchInsert(entities: Seq[Dweller])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
))
        SQL("""insert into dweller(

      ) values (

      )""").batchByName(params: _*).apply()
    }

  def save(entity: Dweller)(implicit session: DBSession = autoSession): Dweller = {
    sql"""
      update
        ${Dweller.table}
      set
        ${column.personId} = ${entity.personId}
      where
        ${column.personId} = ${entity.personId}
      """.update.apply()
    entity
  }

  def destroy(entity: Dweller)(implicit session: DBSession = autoSession): Unit = {
    sql"""delete from ${Dweller.table} where ${column.personId} = ${entity.personId}""".update.apply()
  }
}
