package models

import scalikejdbc._

/** Created by a.kiselev on 01/11/2016. */
case class DwellerLivesInFlat(personId: Int,
                              flatId: Int)

object DwellerLivesInFlat extends SQLSyntaxSupport[DwellerLivesInFlat] {

  def apply(df: ResultName[DwellerLivesInFlat])(rs: WrappedResultSet): DwellerLivesInFlat =
    new DwellerLivesInFlat(
      personId = rs.get(df.personId),
      flatId = rs.get(df.flatId)
    )

  def apply(df: SyntaxProvider[DwellerLivesInFlat])(rs: WrappedResultSet): DwellerLivesInFlat =
    apply(df.resultName)(rs)

  val df = DwellerLivesInFlat.syntax("df")

}
