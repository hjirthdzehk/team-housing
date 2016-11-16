package models

import org.joda.time.DateTime
import scalikejdbc._

/**
  * Created by VladVin on 15.11.2016.
  */
case class ServiceRequest (
    id: Long,
    description: String,
    rating: Option[Int],
    creationDate: DateTime,
    status: Option[Int]
)

object ServiceRequest extends SQLSyntaxSupport[ServiceRequest] {

    def apply(srName: ResultName[ServiceRequest])(rs: WrappedResultSet): ServiceRequest = new ServiceRequest(
        id = rs.get(srName.id),
        description = rs.get(srName.description),
        rating = rs.get(srName.rating),
        creationDate = rs.get(srName.creationDate),
        status = rs.get(srName.status)
    )

    def apply(sr: SyntaxProvider[ServiceRequest])(rs: WrappedResultSet): ServiceRequest = {
        apply(sr.resultName)(rs)
    }

    val sr = ServiceRequest.syntax("sr")

    def create(flatId: Int,
               description: String)
              (implicit session: DBSession = autoSession): ServiceRequest = {
        val id =
            sql"""INSERT INTO ${table}
                  (${column.description}, ${column.rating}, ${column.creationDate}, ${column.status})
                 VALUES (${description}, ${0}, ${DateTime.now}, ${0})
               """
            .updateAndReturnGeneratedKey.apply()

        RequestToFlat.create(id, flatId)

        sql"""SELECT * FROM ${table}
             WHERE ${column.id} = ${id}
           """
            .map(ServiceRequest(sr)).single().apply().get
    }

    def findAllForFlat(flatId: Long)
                      (implicit session: DBSession = autoSession): List[ServiceRequest] = {
        import RequestToFlat.rtf
        sql"""
           SELECT *
           FROM ${table}
           INNER JOIN ${RequestToFlat as rtf}
           ON ${column.id} = ${rtf.requestId}
           WHERE ${rtf.flatId} = ${flatId}
           """
            .map(ServiceRequest(sr)).list().apply()
    }


    def findNextVisitDate(requestId: Long)
                         (implicit session: DBSession = autoSession): Option[DateTime] = {
        import Visited.v
        // Walk through only by IN_PROGRESS service requests
        sql"""
           SELECT ${v.scheduleTime}
           FROM ${Visited as v}
           INNER JOIN ${table}
           ON ${v.serviceRequsetId} = ${column.id}
           WHERE ${column.id} = 1 AND ${column.status} = 1
           ORDER BY ${v.scheduleTime} DESC
           LIMIT 1
           """
                .map(rs => rs.jodaDateTimeOpt("scheduleTime")).single().apply().get
    }

    def getTotalCost(requestId: Long)
                    (implicit session: DBSession = autoSession): Option[Float] = {
        import Visited.v
        sql"""
           SELECT SUM(${v.costs})
           FROM ${Visited as v}
           WHERE ${v.serviceRequsetId} = ${requestId}
           """
            .map(rs => rs.floatOpt(0)).single().apply().get
    }
}
