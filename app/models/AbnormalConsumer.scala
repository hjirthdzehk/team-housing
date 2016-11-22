package models

import scalikejdbc._


case class AbnormalConsumer(personId: Long,
                            meterType: String,
                            consumes: BigDecimal,
                            averageConsumption: BigDecimal)

object AbnormalConsumer  extends SQLSyntaxSupport[AbnormalConsumer] {
  def apply()(rs: WrappedResultSet): AbnormalConsumer =
    AbnormalConsumer(
      personId = rs.get("personId"),
      meterType = rs.get("meterType"),
      consumes = rs.get("consumes"),
      averageConsumption = rs.get("averageConsumption")
    )
  def abnormalConsumers(implicit session: DBSession = autoSession) : List[AbnormalConsumer] = {
    sql"""
       SELECT person_id as personId,
        type as meterType,
        sum as consumes,
        avg as averageConsumption
       FROM (SELECT person.person_id as person_id, meter.type as type, SUM(mr.value) as sum
       	FROM meter_reading as mr
       		JOIN meter ON mr.meter_id = meter.meter_id
       		JOIN flat ON meter.flat_id=flat.flat_id
       		JOIN dweller_lives_in_flat ON flat.flat_id=dweller_lives_in_flat.flat_id
       		JOIN dweller ON dweller_lives_in_flat.person_id = dweller.person_id
       		JOIN person ON person.person_id = dweller.person_id
       		JOIN meter_unit ON meter.meter_unit_id=meter_unit.meter_unit_id
       	GROUP BY person.person_id, meter.type) as sum
       	NATURAL JOIN (SELECT type, AVG(sum)
       	FROM (
       		SELECT p.person_id as person_id, meter.type as type, SUM(mr.value) as sum
       		FROM meter_reading as mr
       			JOIN meter ON mr.meter_id = meter.meter_id
       			JOIN flat ON meter.flat_id=flat.flat_id
       			JOIN dweller_lives_in_flat ON flat.flat_id=dweller_lives_in_flat.flat_id
       			JOIN dweller ON dweller_lives_in_flat.person_id = dweller.person_id
       			JOIN person as p ON p.person_id = dweller.person_id
       			JOIN meter_unit ON meter.meter_unit_id=meter_unit.meter_unit_id
       		GROUP BY p.person_id, meter.type) as sums
       	GROUP BY type) as avg
       WHERE sum>=avg
      """.map(apply()).list().apply()
  }
}