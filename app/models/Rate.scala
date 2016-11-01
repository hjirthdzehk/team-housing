package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class Rate(
  rateId: Int,
  value: Option[BigDecimal] = None,
  dateFrom: Option[DateTime] = None,
  dateTo: Option[DateTime] = None,
  meterUnitId: Int) {

  def save()(implicit session: DBSession = Rate.autoSession): Rate = Rate.save(this)(session)

  def destroy()(implicit session: DBSession = Rate.autoSession): Unit = Rate.destroy(this)(session)

}


object Rate extends SQLSyntaxSupport[Rate] {

  override val tableName = "rate"

  override val columns = Seq("rate_id", "value", "date_from", "date_to", "meter_unit_id")

  def apply(r: SyntaxProvider[Rate])(rs: WrappedResultSet): Rate = apply(r.resultName)(rs)
  def apply(r: ResultName[Rate])(rs: WrappedResultSet): Rate = new Rate(
    rateId = rs.get(r.rateId),
    value = rs.get(r.value),
    dateFrom = rs.get(r.dateFrom),
    dateTo = rs.get(r.dateTo),
    meterUnitId = rs.get(r.meterUnitId)
  )

  val r = Rate.syntax("r")

  override val autoSession = AutoSession

  def find(rateId: Int)(implicit session: DBSession = autoSession): Option[Rate] = {
    sql"""select ${r.result.*} from ${Rate as r} where ${r.rateId} = ${rateId}"""
      .map(Rate(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Rate] = {
    sql"""select ${r.result.*} from ${Rate as r}""".map(Rate(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Rate.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Rate] = {
    sql"""select ${r.result.*} from ${Rate as r} where ${where}"""
      .map(Rate(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Rate] = {
    sql"""select ${r.result.*} from ${Rate as r} where ${where}"""
      .map(Rate(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Rate as r} where ${where}"""
      .map(_.long(1)).single.apply().get
  }

  def create(
    value: Option[BigDecimal] = None,
    dateFrom: Option[DateTime] = None,
    dateTo: Option[DateTime] = None,
    meterUnitId: Int)(implicit session: DBSession = autoSession): Rate = {
    val generatedKey = sql"""
      insert into ${Rate.table} (
        ${column.value},
        ${column.dateFrom},
        ${column.dateTo},
        ${column.meterUnitId}
      ) values (
        ${value},
        ${dateFrom},
        ${dateTo},
        ${meterUnitId}
      )
      """.updateAndReturnGeneratedKey.apply()

    Rate(
      rateId = generatedKey.toInt,
      value = value,
      dateFrom = dateFrom,
      dateTo = dateTo,
      meterUnitId = meterUnitId)
  }

  def batchInsert(entities: Seq[Rate])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'value -> entity.value,
        'dateFrom -> entity.dateFrom,
        'dateTo -> entity.dateTo,
        'meterUnitId -> entity.meterUnitId))
        SQL("""insert into rate(
        value,
        date_from,
        date_to,
        meter_unit_id
      ) values (
        {value},
        {dateFrom},
        {dateTo},
        {meterUnitId}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: Rate)(implicit session: DBSession = autoSession): Rate = {
    sql"""
      update
        ${Rate.table}
      set
        ${column.rateId} = ${entity.rateId},
        ${column.value} = ${entity.value},
        ${column.dateFrom} = ${entity.dateFrom},
        ${column.dateTo} = ${entity.dateTo},
        ${column.meterUnitId} = ${entity.meterUnitId}
      where
        ${column.rateId} = ${entity.rateId}
      """.update.apply()
    entity
  }

  def destroy(entity: Rate)(implicit session: DBSession = autoSession): Unit = {
    sql"""delete from ${Rate.table} where ${column.rateId} = ${entity.rateId}""".update.apply()
  }

}
