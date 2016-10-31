package models

import org.joda.time.DateTime
import scalikejdbc._

/** Created by a.kiselev on 31/10/2016. */
case class Person(personId: Int,
                  name: String,
                  surname: String,
                  paternalName: String,
                  registrationDate: DateTime) {

  def findDweller: Option[Dweller] = Dweller.findById(personId)

}


object Person extends SQLSyntaxSupport[Person] {

  def apply(p: ResultName[Person])(rs: WrappedResultSet): Person =
    new Person(
      personId = rs.get(p.personId),
      name = rs.get(p.name),
      surname = rs.get(p.name),
      paternalName = rs.get(p.paternalName),
      registrationDate = rs.get(p.registrationDate)
    )

  def apply(p: SyntaxProvider[Person])(rs: WrappedResultSet): Person =
    apply(p.resultName)(rs)

  val p = Person.syntax("p")

  def findById(personId: Int)(implicit session: DBSession = autoSession): Option[Person] =
    sql"""select ${p.result.*} from ${Person as p}
         where ${p.personId} = ${personId}
       """.map(Person(p)).single().apply()

}
