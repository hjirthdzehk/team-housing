package models

import org.joda.time.DateTime
import scalikejdbc._

case class MeterReading(meterReadingId: Long,
                        value: BigDecimal,
                        date: DateTime,
                        paid: Boolean,
                        meterId: Int)

object MeterReading extends SQLSyntaxSupport[MeterReading] {
  def apply(mr: SyntaxProvider[MeterReading])(rs: WrappedResultSet): MeterReading =
    apply(mr.resultName)(rs)

  def apply(mr: ResultName[MeterReading])(rs: WrappedResultSet): MeterReading =
    MeterReading(
      meterReadingId = rs.get(mr.meterReadingId),
      value = rs.get(mr.value),
      date = rs.get(mr.date),
      paid = rs.get(mr.paid),
      meterId = rs.get(mr.meterId)
    )

  val mr = MeterReading.syntax("mr")

  def create(value: BigDecimal,
             date: DateTime,
             paid: Boolean,
             meterId: Integer
            )(implicit session: DBSession = autoSession): MeterReading = {
    val meterReadingId =
      sql"""insert into ${table} (${column.value}, ${column.date}, ${column.paid}, ${column.meterId})
            values (${value}, ${date}, ${paid}, ${meterId})"""
      .updateAndReturnGeneratedKey.apply()

    MeterReading(meterReadingId, value, date, paid, meterId)
  }

  def listByMeterId(meterId: Int)(implicit session: DBSession = autoSession): Seq[MeterReading] =
    sql"""select ${mr.result.*} from ${MeterReading as mr}
          where ${mr.meterId} = ${meterId}
       """.map(MeterReading(mr)).list().apply()

}