package models

import scalikejdbc._

/** Created by a.kiselev on 30/10/2016. */
case class Building(
  cladrId: Int,
  buildingId: Int
) {}

object Building extends SQLSyntaxSupport[Building] {

  def apply(b: ResultName[Building])(rs: WrappedResultSet): Building = {
    new Building(
      cladrId = rs.get(b.cladrId),
      buildingId = rs.get(b.buildingId)
    )
  }

  def apply(b: SyntaxProvider[Building])(rs: WrappedResultSet): Building = {
    apply(b.resultName)(rs)
  }

  val b = Building.syntax("b")

  def find(cladrId: Int)(implicit session: DBSession = autoSession) =
    sql"""select ${b.result.*} from ${Building as b}
          where ${b.cladrId} = ${cladrId}
      """.map(Building(b)).single().apply()

  def create(
    cladrId: Int,
    buildingId: Int
  )(implicit session: DBSession = autoSession) = {
    sql"""insert into ${table} (${column.cladrId}, ${column.buildingId})
            values (${cladrId}, ${buildingId})
         """.update.apply()
  }

  def destroy(
    cladrId: Int,
    buildingId: Int
  )(implicit session: DBSession = autoSession) = {
    sql"""delete from $table
          where ${column.cladrId} = $cladrId and
                ${column.buildingId} = $buildingId
       """.update.apply()
  }

}
