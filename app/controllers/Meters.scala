package controllers

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.play2.json4s.native._
import models._
import org.joda.time.DateTime
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scalikejdbc._
import models.Meter.autoSession

case class MeterViewModel(id: Integer,
                          title: String,
                          unit: String)

case class MeterGroup(title: String,
                      meters: List[MeterViewModel])

@Singleton
class Meters @Inject() (json4s: Json4s) extends Controller {
  import json4s._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  def listByFlatId(flatId: Int) = Action {
    Ok(Extraction.decompose(
      Meter.listByFlatId(flatId)
        .groupBy(m => m.`type`)
        .map{ case (title: String, meters: List[Meter]) =>
          MeterGroup(
            title,
            meters.map{ case Meter(meterId, _, _, _, meterUnitId, active, _) =>
              MeterViewModel(meterId, title, MeterUnit.get(meterUnitId).description)}
          )
        }))
  }

  def getReadingsCosts(flatId: Int)(implicit session: DBSession = autoSession) = Action {
    val (mr, m, r) = (MeterReading.mr, Meter.m, Rate.r)

    val query =
      sql"""
           select ${m.meterId}, ${mr.date}, ${mr.value} * ${r.value} as cost
           from ${MeterReading as mr}
            natural join ${Meter as m}
            natural join ${Rate as r}
           where ${m.flatId} = ${flatId}
            and ${r.dateFrom} <= ${mr.date}
            and ${r.dateTo} >= ${mr.date}
         """.map(rs => (
        rs.get(m.meterId): Int,
        rs.get(mr.date): DateTime,
        rs.get("cost"): Int
      )).list().apply()

    val result = query.groupBy(_._1)
      .map{case (meterId, everything) =>
        meterId -> everything.map{ case (_, date, cost) => (date, cost) }.distinct }

    Ok(Extraction.decompose(result))
  }

  case class MeterReadingForm(meterId: Int,
                              value: BigDecimal,
                              date: DateTime)

  private val meterReadingForm = Form(
    mapping(
      "meterId" -> number,
      "value" -> bigDecimal,
      "date" -> jodaDate
    )(MeterReadingForm.apply)(MeterReadingForm.unapply)
  )

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
}