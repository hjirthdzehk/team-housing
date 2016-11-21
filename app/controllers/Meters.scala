package controllers

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.play2.json4s.native._
import models.Meter.autoSession
import models._
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import scalikejdbc._

case class MeterReadingCost(title: String,
                            date: DateTime,
                            cost: BigDecimal)

object MeterReadingCost extends SQLSyntaxSupport[MeterReadingCost] {
  def apply(mr: SyntaxProvider[MeterReading], m: SyntaxProvider[Meter]): (WrappedResultSet) => MeterReadingCost =
    apply(mr.resultName, m.resultName)

  def apply(mr: ResultName[MeterReading], m: ResultName[Meter])(rs: WrappedResultSet): MeterReadingCost =
    MeterReadingCost(
      rs.get(m.title): String,
      rs.get(mr.date): DateTime,
      rs.get("cost"): BigDecimal
    )
}

case class MeterListItem(title: String,
                         meterId: Int)

object MeterListItem extends SQLSyntaxSupport[Meter] {
  def apply( m: SyntaxProvider[Meter]): (WrappedResultSet) => MeterListItem =
    apply(m.resultName)

  def apply(m: ResultName[Meter])(rs: WrappedResultSet): MeterListItem =
    MeterListItem(
      rs.get(m.title): String,
      rs.get(m.meterId): Int
    )
}

@Singleton
class Meters @Inject() (json4s: Json4s) extends Controller {
  import org.json4s._

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  val meterCreateForm = Form(
    mapping(
      "title" -> text,
      "type" -> text,
      "meterUnitId" -> number,
      "flatId" -> number
    )(MeterCreateForm.apply)(MeterCreateForm.unapply)
  )
  private val meterReadingForm = Form(
    mapping(
      "meterId" -> number,
      "value" -> bigDecimal,
      "date" -> jodaDate
    )(MeterReadingForm.apply)(MeterReadingForm.unapply)
  )

  def getMeters(flatId: Int)(implicit session: DBSession = autoSession) = Action {
    implicit val meterListItemFormat = Json.format[MeterListItem]
    import Meter.m
    val query =
      sql"""
            select ${m.result.title}, ${m.result.meterId}
            from ${Meter as m}
            where ${m.flatId} = ${flatId}
      """.map(MeterListItem(m)).list.apply()
    Ok(Json.obj("meterListItems" -> query))
  }

  def getReadingsCosts(meterId: Int,
                       dateFromStr: Option[String],
                       dateToStr: Option[String])(implicit session: DBSession = autoSession) = Action {
    import Meter.m
    import MeterReading.mr
    import Rate.r
    implicit val meterReadingCostFormat = Json.format[MeterReadingCost]

    val dateFrom = DateTime.parse(dateFromStr.get)
    val dateTo = DateTime.parse(dateToStr.get)
    val query =
      sql"""
          select ${m.result.title}, ${mr.result.date}, mr.value * r.value as cost
          from ${MeterReading as mr}, ${Meter as m} , ${Rate as r}
          where ${m.meterId} = ${mr.meterId} and ${m.meterUnitId} = ${r.meterUnitId}
          and ${r.dateFrom} <= ${mr.date} and ${r.dateTo} >= ${mr.date}
          and ${mr.paid} = false and ${m.meterId} = ${meterId}
          and ${mr.date} >= ${dateFrom} and ${mr.date} <= ${dateTo}
          order by date desc;
      """.map(MeterReadingCost(mr, m)).list.apply()
    Ok(Json.obj("readingCosts" -> query))
  }

  def saveReading = Action { implicit req =>
    meterReadingForm.bindFromRequest.fold(
      formWithErrors => BadRequest(
        formWithErrors.errors
          .foldLeft("")((res, message) =>
            res + message.message + ",")),
      form => {
        MeterReading.create(
          value = form.value,
          date = form.date,
          paid = false,
          meterId = form.meterId)
        Ok
      }
    )
  }

  def create() =
    Action {
      implicit req =>
        meterCreateForm.bindFromRequest.fold(
          formWithErrors => BadRequest("Something bad happened"),
          form => {
            val meter = Meter.create(
              form.title,
              form.`type`,
              form.meterUnitId,
              form.flatId)
            Ok//(Extraction.decompose(meter))
          }
        )
    }

  def delete(meterId: Int) = Action {
    Meter.find(meterId) match {
      case Some(x) =>
        Meter.delete(meterId)
        NoContent
      case _ =>
        NotFound
    }
  }

  case class MeterReadingForm(meterId: Int,
                              value: BigDecimal,
                              date: DateTime)

  case class MeterCreateForm(title: String,
                             `type`: String,
                             meterUnitId: Int,
                             flatId: Int)
}