package models
import scalikejdbc._

case class Billing (personId: Long,
                    personName: String,
                    personSurname: String,
                    meterId: Long,
                    meterTitle: String,
                    flatNumber : String,
                    debt: BigDecimal,
                    unpaidDelta: BigDecimal,
                    meterUnits: String,
                    rate: BigDecimal
                   )

object Billing  extends SQLSyntaxSupport[Billing]{
  def apply()(rs: WrappedResultSet): Billing =
    Billing(
      personId = rs.get("personId"),
      personName = rs.get("personName"),
      personSurname = rs.get("personSurname"),
      meterId = rs.get("meterId"),
      meterTitle = rs.get("meterTitle"),
      flatNumber = rs.get("flatNumber"),
      debt = rs.get("debt"),
      unpaidDelta = rs.get("unpaidDelta"),
      meterUnits = rs.get("meterUnits"),
      rate = rs.get("rate")
    )
    def getPersonDebt(personId: Long)(implicit session: DBSession = autoSession) : List[Billing] = {
      sql"""
          SELECT p.person_id as "personId",
                            p.name as "personName",
                            p.surname as "personSurname",
                            m1.meter_id as "meterId",
                            meter.title as "meterTitle",
                            flat.flat_number as "flatNumber",
                            SUM(m2.value-m1.value) as "unpaidDelta",
                            meter_unit.description as "meterUnits",
                            SUM((m2.value-m1.value)*rate.value) as "debt",
                            rate.value as "rate"
                          FROM meter_reading as m1
                          	CROSS JOIN meter_reading as m2
                          	JOIN meter ON m1.meter_id = meter.meter_id
                          	JOIN rate ON meter.meter_unit_id = rate.meter_unit_id
                          	JOIN flat ON meter.flat_id=flat.flat_id
                          	JOIN dweller_lives_in_flat ON flat.flat_id=dweller_lives_in_flat.flat_id
                          	JOIN dweller ON dweller_lives_in_flat.person_id = dweller.person_id
                          	JOIN person as p ON p.person_id = dweller.person_id
                          	JOIN meter_unit ON meter.meter_unit_id=meter_unit.meter_unit_id
                          WHERE m1.paid = FALSE
                          	AND m1.meter_id = m2.meter_id
                          	AND m2.date = (
                          		SELECT MAX(date)
                          		FROM meter_reading as m3
                          		WHERE m3.date < m1.date
                          			AND m3.meter_id=m1.meter_id)
                          	AND m1.date < rate.date_to
                          	AND m1.date > rate.date_from
                           AND p.person_id = ${personId}
                          GROUP BY m1.meter_id, meter.title, p.person_id, p.name, p.surname, flat.flat_number, meter_unit.description, rate.value
        """.map(apply()).list().apply()
    }

  def getPersonDebts()(implicit session: DBSession = autoSession) : List[Billing] = {
    sql"""
                    SELECT p.person_id as "personId",
                                   p.name as "personName",
                                   p.surname as "personSurname",
                                   m1.meter_id as "meterId",
                                   meter.title as "meterTitle",
                                   flat.flat_number as "flatNumber",
                                   SUM(m2.value-m1.value) as "unpaidDelta",
                                   meter_unit.description as "meterUnits",
                                   SUM((m2.value-m1.value)*rate.value) as "debt",
                                   rate.value as "rate"
                                 FROM meter_reading as m1
                                 	CROSS JOIN meter_reading as m2
                                 	JOIN meter ON m1.meter_id = meter.meter_id
                                 	JOIN rate ON meter.meter_unit_id = rate.meter_unit_id
                                 	JOIN flat ON meter.flat_id=flat.flat_id
                                 	JOIN dweller_lives_in_flat ON flat.flat_id=dweller_lives_in_flat.flat_id
                                 	JOIN dweller ON dweller_lives_in_flat.person_id = dweller.person_id
                                 	JOIN person as p ON p.person_id = dweller.person_id
                                 	JOIN meter_unit ON meter.meter_unit_id=meter_unit.meter_unit_id
                                 WHERE m1.paid = FALSE
                                 	AND m1.meter_id = m2.meter_id
                                	AND m2.date = (
                                 		SELECT MAX(date)
                                 		FROM meter_reading as m3
                                 		WHERE m3.date < m1.date
                                 			AND m3.meter_id=m1.meter_id)
                                 	AND m1.date < rate.date_to
                                 	AND m1.date > rate.date_from
                                 GROUP BY m1.meter_id, meter.title, p.person_id, p.name, p.surname, flat.flat_number, meter_unit.description, rate.value
        """.map(apply()).list().apply()
  }
}