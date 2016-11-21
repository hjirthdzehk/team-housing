package models

import org.joda.time.LocalDate
import scalikejdbc._

case class Person(personId: Int,
                  name: String,
                  surname: String,
                  paternalName: String,
                  registrationDate: LocalDate = LocalDate.now(),
                  email: String,
                  passwordHash: String) {

  def findDweller = Dweller.find(personId)

  def save()(implicit session: DBSession = Person.autoSession): Person = Person.save(this)(session)

  def destroy()(implicit session: DBSession = Person.autoSession): Unit = Person.destroy(this)(session)

}


object Person extends SQLSyntaxSupport[Person] {

  override val tableName = "person"
  override val autoSession = AutoSession
  val p = Person.syntax("p")

  def apply(p: SyntaxProvider[Person])(rs: WrappedResultSet): Person = apply(p.resultName)(rs)

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

  def apply(p: ResultName[Person])(rs: WrappedResultSet): Person = new Person(
    personId = rs.get(p.personId),
    name = rs.get(p.name),
    surname = rs.get(p.surname),
    paternalName = rs.get(p.paternalName),
    registrationDate = rs.get(p.registrationDate),
    email = rs.get(p.email),
    passwordHash = rs.get(p.passwordHash)
  )

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Person as p} where ${where}"""
      .map(_.long(1)).single.apply().get
  }

  def create(
              name: String,
              surname: String,
              paternalName: String,
              email: String,
              passwordHash: String)(implicit session: DBSession = autoSession): Person = {
    val registrationDate = LocalDate.now()
    val generatedKey = sql"""
      INSERT INTO ${Person.table} (
        ${column.name},
        ${column.surname},
        ${column.paternalName},
        ${column.registrationDate},
        ${column.email},
        ${column.passwordHash}
      ) VALUES (
        ${name},
        ${surname},
        ${paternalName},
        ${registrationDate},
        ${email},
        ${passwordHash}
      )
      """.updateAndReturnGeneratedKey.apply()

    Person(
      personId = generatedKey.toInt,
      name = name,
      surname = surname,
      paternalName = paternalName,
      registrationDate = registrationDate,
      email = email,
      passwordHash = passwordHash
    )
  }

  def batchInsert(entities: Seq[Person])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'surname -> entity.surname,
        'paternalName -> entity.paternalName,
        'registrationDate -> entity.registrationDate))
    SQL(
      """insert into person(
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
      UPDATE
        ${Person.table}
      SET
        ${column.personId} = ${entity.personId},
        ${column.name} = ${entity.name},
        ${column.surname} = ${entity.surname},
        ${column.paternalName} = ${entity.paternalName},
        ${column.registrationDate} = ${entity.registrationDate},
        ${column.email} = ${entity.email},
        ${column.passwordHash} = ${entity.passwordHash}
      WHERE
        ${column.personId} = ${entity.personId}
      """.update.apply()
    entity
  }

  def destroy(entity: Person)(implicit session: DBSession = autoSession): Unit = {
    sql"""DELETE FROM ${Person.table} WHERE ${column.personId} = ${entity.personId}""".update.apply()
  }

}
