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

@Singleton
class Meters @Inject() (json4s: Json4s) extends Controller {
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

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