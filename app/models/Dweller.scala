package models

import scalikejdbc._

/** Created by a.kiselev on 31/10/2016. */
case class Dweller(personId: Int) {

  def getPerson: Person = Person.findById(personId).get

  def listFlats: Seq[Flat] = Flat.listByPersonId(personId)

}


object Dweller extends SQLSyntaxSupport[Dweller] {

  def apply(d: ResultName[Dweller])(rs: WrappedResultSet): Dweller =
    new Dweller(personId = rs.get(d.personId))

  def apply(d: SyntaxProvider[Dweller])(rs: WrappedResultSet): Dweller =
    apply(d.resultName)(rs)

  val d = Dweller.syntax("d")

  def findById(personId: Int)(implicit session: DBSession = autoSession): Option[Dweller] =
    sql"""select ${d.result.*} from ${Dweller as d}
          where ${d.personId} = ${personId}
      """.map(Dweller(d)).single().apply()

}