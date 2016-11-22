package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import models.Visited
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

case class VisitedViewModel(totalCost: Double,
                            visits: List[Visited])

class Visiteds @Inject()(json4s: Json4s) extends Controller {

  import json4s._

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  case class VisitCreationForm(requestId: Long,
                               scheduleTime: DateTime,
                               costs: BigDecimal)

  private val visitCreateForm = Form(
    mapping(
      "requestId" -> longNumber,
      "scheduleTime" -> jodaDate,
      "costs" -> bigDecimal
    )(VisitCreationForm.apply)(VisitCreationForm.unapply)
  )

  def all(requestId: Long) = Action {
    val visits = Visited.findAll(requestId)
    val totalCost = Visited.getTotalCost(requestId)
    Ok(Extraction.decompose(VisitedViewModel(totalCost, visits)))
  }

  def create() = Action {
    visitCreateForm.bindFromRequest().fold(
      formWithErrors => BadRequest(
        formWithErrors.errors
            .foldLeft("")((res, message) =>
              res + message.message + ",")),
      form => {
        Visited.create(form.requestId, form.costs.toFloat, form.scheduleTime)
      }
    )
    Ok
  }
}
