package models

import org.joda.time.DateTime
import scalikejdbc._
import sun.font.LayoutPathImpl.EndType

case class Visited (
    visitId: Long,
    serviceRequsetId: Long,
    scheduleTime: DateTime,
    outTime: Option[DateTime] = None,
    startTime: Option[DateTime] = None,
    endTime: Option[DateTime] = None,
    costs: Float
)

object Visited extends SQLSyntaxSupport[Visited] {
    def apply(vName: ResultName[Visited])(rs: WrappedResultSet): Visited = new Visited(
        visitId = rs.get(vName.visitId),
        serviceRequsetId = rs.get(vName.serviceRequsetId),
        scheduleTime = rs.get(vName.scheduleTime),
        outTime = rs.get(vName.outTime),
        startTime = rs.get(vName.startTime),
        endTime = rs.get(vName.endTime),
        costs = rs.get(vName.costs)
    )

    def apply(v: SyntaxProvider[Visited])(rs: WrappedResultSet): Visited = {
        apply(v.resultName)(rs)
    }

    val v = Visited.syntax("r")

    def create(serviceRequsetId: Long, costs: Float,
               scheduleTime: DateTime, outTime: Option[DateTime] = None,
               startTime:Option[DateTime] = None, endTime: Option[DateTime] = None)
              (implicit session: DBSession = autoSession): Visited = {
        val id =
            sql"""
                 INSERT INTO ${table} (${column.serviceRequsetId}, ${column.scheduleTime},
                 ${column.outTime}, ${column.startTime}, ${column.endTime}, ${column.costs})
                 VALUES(${serviceRequsetId}, ${scheduleTime}, ${outTime},
                    ${startTime}, ${endTime}, ${costs})
               """
            .updateAndReturnGeneratedKey.apply()

        Visited(visitId = id, serviceRequsetId = serviceRequsetId, costs = costs,
            scheduleTime = scheduleTime, outTime = outTime,
            startTime = startTime, endTime = endTime)
    }

    def findAll(requestId: Long)
               (implicit session: DBSession = autoSession): List[Visited] = {
        sql"""
           SELECT ${v.result.*} FROM ${Visited as v}
           WHERE ${v.serviceRequsetId} = ${requestId}
           """
            .map(Visited(v)).list().apply()
    }

    def applyToDouble(rs: WrappedResultSet): Double = rs.get("totalCosts")
    def getTotalCost(requestId: Long)
                    (implicit session: DBSession = autoSession): Double = {
      val resultOption = sql"""
           SELECT SUM(costs) AS totalCosts
           FROM ${Visited as v}
           GROUP BY ${v.serviceRequsetId}
           HAVING ${v.serviceRequsetId}=${requestId}
           """
        .map(applyToDouble).single().apply()
        if (resultOption.isDefined) resultOption.get else 0.0
    }
}