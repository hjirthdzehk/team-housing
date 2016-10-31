package models

import scalikejdbc.{DBSession, WrappedResultSet, _}

case class MeterUnit(meterUnitId: Int,
                     description: String)

object MeterUnit extends SQLSyntaxSupport[MeterUnit]{
  def apply(m: ResultName[MeterUnit])(rs: WrappedResultSet): MeterUnit = {
    new MeterUnit(
      meterUnitId = rs.get(m.meterUnitId),
      description = rs.get(m.description)
    )
  }

  def apply(m: SyntaxProvider[MeterUnit])(rs: WrappedResultSet): MeterUnit = {
    apply(m.resultName)(rs)
  }

  val mu = MeterUnit.syntax("mu")

  def get(id: Int)(implicit session: DBSession = autoSession): MeterUnit =
    sql"""select ${mu.result.*} from ${MeterUnit as mu}
          where ${mu.meterUnitId} = ${id}
      """.map(MeterUnit(mu)).single().apply().get
}
