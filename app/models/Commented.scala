package models

import scalikejdbc._
import org.joda.time.DateTime

/**
  * Created by VladVin on 15.11.2016.
  */
case class Commented (
    id: Long,
    requestId: Long,
    personId: Long,
    text: String,
    date: DateTime
)

object Commented extends SQLSyntaxSupport[Commented] {
    def apply(cName: ResultName[Commented])
             (rs: WrappedResultSet): Commented = new Commented (
        id = rs.get(cName.id),
        requestId = rs.get(cName.requestId),
        personId = rs.get(cName.personId),
        text = rs.get(cName.text),
        date = rs.get(cName.date)
    )

    def apply(c: SyntaxProvider[Commented])(rs: WrappedResultSet): Commented = {
        apply(c.resultName)(rs)
    }

    val c = Commented.syntax("c")

    def create(requestId: Long,
               personId: Long,
               text: String,
               date: DateTime)
              (implicit session: DBSession = autoSession): Commented = {
        val id =
            sql"""
                 INSERT INTO ${table}
                 (${column.requestId}, ${column.personId},
                 ${column.text}, ${column.date})
                 VALUES (${requestId}, ${personId}, ${text}, ${date})
               """
            .updateAndReturnGeneratedKey().apply()

        Commented(id = id, requestId = requestId, personId = personId, text = text, date = date)
    }

    def findAll(requestId: Long)
               (implicit session: DBSession = autoSession): List[Commented] = {
        sql"""
             SELECT * FROM ${table}
             WHERE ${column.requestId} = ${requestId}
           """
            .map(Commented(c)).list().apply()
    }
}
