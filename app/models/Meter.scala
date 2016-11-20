package models

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

case class Meter(meterId: Int,
                 installationDate: LocalDate,
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

  def create(
              title: String,
              `type`: String,
              meterUnitId: Int,
              flatId: Int)(implicit session: DBSession = autoSession): Meter = {
    val installationDate = LocalDate.now()
    val generatedKey = sql"""
      INSERT INTO ${Meter.table} (
        ${column.title},
        ${column.`type`},
        ${column.installationDate},
        ${column.meterUnitId},
        ${column.active},
        ${column.flatId}
      ) VALUES (
        ${title},
        ${`type`},
        ${installationDate},
        ${meterUnitId},
        ${true},
        ${flatId}
      )
      """.updateAndReturnGeneratedKey.apply()

    Meter(generatedKey.toInt,
      installationDate,
      `type`,
      title,
      meterUnitId,
      active = true, // isActive - why would we add inactive meter?
      flatId
    )
  }

  def find(meterId: Int)(implicit  session: DBSession = autoSession): Option[Meter] =
    sql"""SELECT ${m.result.*} FROM ${Meter as m}
           WHERE ${m.meterId} = ${meterId}
      """.map(Meter(m)).single().apply()

  def delete(meterId: Int)(implicit  session: DBSession = autoSession): Unit = {
    sql"""DELETE FROM ${Meter.table}
          WHERE ${column.meterId} = ${meterId}
      """.update.apply()
  }
}
