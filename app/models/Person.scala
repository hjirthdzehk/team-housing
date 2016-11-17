package models

import scalikejdbc._
import org.joda.time.LocalDate

case class Person(
  personId: Int,
  name: String,
  surname: String,
  paternalName: String,
  registrationDate: LocalDate = LocalDate.now()) {

  def findDweller = Dweller.find(personId)

  def save()(implicit session: DBSession = Person.autoSession): Person = Person.save(this)(session)

  def destroy()(implicit session: DBSession = Person.autoSession): Unit = Person.destroy(this)(session)

}


object Person extends SQLSyntaxSupport[Person] {

  override val tableName = "person"

  override val columns = Seq("person_id", "name", "surname", "paternal_name", "registration_date")

  def apply(p: SyntaxProvider[Person])(rs: WrappedResultSet): Person = apply(p.resultName)(rs)
  def apply(p: ResultName[Person])(rs: WrappedResultSet): Person = new Person(
    personId = rs.get(p.personId),
    name = rs.get(p.name),
    surname = rs.get(p.surname),
    paternalName = rs.get(p.paternalName),
    registrationDate = rs.get(p.registrationDate)
  )

  val p = Person.syntax("p")

  override val autoSession = AutoSession

  def find(personId: Int)(implicit session: DBSession = autoSession): Option[Person] = {
    sql"""select ${p.result.*} from ${Person as p} where ${p.personId} = ${personId}"""
      .map(Person(p.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Person] = {
    sql"""select ${p.result.*} from ${Person as p}""".map(Person(p.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Person.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Person] = {
    sql"""select ${p.result.*} from ${Person as p} where ${where}"""
      .map(Person(p.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Person] = {
    sql"""select ${p.result.*} from ${Person as p} where ${where}"""
      .map(Person(p.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Person as p} where ${where}"""
      .map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    surname: String,
    paternalName: String)(implicit session: DBSession = autoSession): Person = {
    val registrationDate = LocalDate.now()
    val generatedKey = sql"""
      insert into ${Person.table} (
        ${column.name},
        ${column.surname},
        ${column.paternalName},
        ${column.registrationDate}
      ) values (
        ${name},
        ${surname},
        ${paternalName},
        ${registrationDate}
      )
      """.updateAndReturnGeneratedKey.apply()

    Person(
      personId = generatedKey.toInt,
      name = name,
      surname = surname,
      paternalName = paternalName,
      registrationDate = registrationDate)
  }

  def batchInsert(entities: Seq[Person])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'surname -> entity.surname,
        'paternalName -> entity.paternalName,
        'registrationDate -> entity.registrationDate))
        SQL("""insert into person(
        name,
        surname,
        paternal_name,
        registration_date
      ) values (
        {name},
        {surname},
        {paternalName},
        {registrationDate}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: Person)(implicit session: DBSession = autoSession): Person = {
    sql"""
      update
        ${Person.table}
      set
        ${column.personId} = ${entity.personId},
        ${column.name} = ${entity.name},
        ${column.surname} = ${entity.surname},
        ${column.paternalName} = ${entity.paternalName},
        ${column.registrationDate} = ${entity.registrationDate}
      where
        ${column.personId} = ${entity.personId}
      """.update.apply()
    entity
  }

  def destroy(entity: Person)(implicit session: DBSession = autoSession): Unit = {
    sql"""delete from ${Person.table} where ${column.personId} = ${entity.personId}""".update.apply()
  }

}
