package models

import scalikejdbc._

/** Created by a.kiselev on 01/11/2016. */
case class DwellerLivesInFlat(personId: Long,
                              flatId: Long)

object DwellerLivesInFlat extends SQLSyntaxSupport[DwellerLivesInFlat] {

  def apply(df: ResultName[DwellerLivesInFlat])(rs: WrappedResultSet): DwellerLivesInFlat =
    new DwellerLivesInFlat(
      personId = rs.get(df.personId),
      flatId = rs.get(df.flatId)
    )

  def apply(df: SyntaxProvider[DwellerLivesInFlat])(rs: WrappedResultSet): DwellerLivesInFlat =
    apply(df.resultName)(rs)

  val df = DwellerLivesInFlat.syntax("df")

  def find(flatId: Long, personId: Long)
          (implicit session: DBSession = autoSession): Option[DwellerLivesInFlat] = {
    sql"""
         SELECT ${df.result.*}
         FROM ${DwellerLivesInFlat as df}
         WHERE ${df.flatId} = ${flatId}
         AND ${df.personId} = ${personId}
       """
        .map(DwellerLivesInFlat(df)).single().apply()
  }

  def create(flatId: Long, personId: Long)
            (implicit session: DBSession = autoSession): Option[DwellerLivesInFlat] = {
    DwellerLivesInFlat.find(flatId, personId) match {
      case Some(_) => None
      case None => {
        sql"""
         INSERT INTO ${table}
         (${column.personId}, ${column.flatId})
         VALUES (${personId}, ${flatId})
       """.updateAndReturnGeneratedKey().apply()
        Some(DwellerLivesInFlat(personId, flatId))
      }
    }
  }
}
