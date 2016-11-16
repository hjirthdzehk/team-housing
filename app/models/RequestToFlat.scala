package models

import scalikejdbc._

/**
  * Created by VladVin on 16.11.2016.
  */
case class RequestToFlat (
    requestId: Long,
    flatId: Long)

object RequestToFlat extends SQLSyntaxSupport[RequestToFlat] {
    def apply(rtfName: ResultName[RequestToFlat])(rs: WrappedResultSet): RequestToFlat = new RequestToFlat(
        requestId = rs.get(rtfName.requestId),
        flatId = rs.get(rtfName.flatId)
    )

    def apply(rtf: SyntaxProvider[RequestToFlat])(rs: WrappedResultSet): RequestToFlat = {
        apply(rtf.resultName)(rs)
    }

    val rtf = RequestToFlat.syntax("rtf")

    def create(requestId: Long, flatId: Long)
              (implicit session: DBSession = autoSession): RequestToFlat = {
        sql"""INSERT INTO ${table}
             (${column.requestId}, ${column.flatId})
             VALUES (${requestId}, ${flatId})"""
            .update().apply()
        RequestToFlat(requestId = requestId, flatId = flatId)
    }
}