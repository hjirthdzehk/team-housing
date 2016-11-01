package models

import org.joda.time.DateTime
import scalikejdbc._

case class Meter(meterId: Int,
                 installationDate: DateTime,
                 `type`: String,
                 title: String,
                 meterUnitId: Int,
                 active: Boolean,
                 flatId: Int) {
  def listReadings = MeterReading.listByMeterId(meterId)

  def getUnit = MeterUnit.get(meterUnitId)
}


object Meter extends SQLSyntaxSupport[Meter] {
  def apply(m: ResultName[Meter])(rs: WrappedResultSet): Meter =
    new Meter(
      meterId = rs.get(m.meterId),
      installationDate = rs.get(m.installationDate),
      `type` = rs.get(m.`type`),
      title = rs.get(m.title),
      meterUnitId = rs.get(m.meterUnitId),
      active = rs.get(m.active),
      flatId = rs.get(m.flatId)
    )

  def apply(m: SyntaxProvider[Meter])(rs: WrappedResultSet): Meter =
    apply(m.resultName)(rs)

  val m = Meter.syntax("m")

  def listByFlatId(flatId: Int)(implicit session: DBSession = autoSession): Seq[Meter] =
    sql"""select ${m.result.*} from ${Meter as m}
          where ${m.flatId} = ${flatId} AND ${m.active} = TRUE
      """.map(Meter(m)).list.apply()
}
