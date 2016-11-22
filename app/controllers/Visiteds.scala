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

case class VisitedViewModel(visitId: Long,
                            serviceRequsetId: Long,
                            scheduleTime: DateTime,
                            outTime: DateTime,
                            startTime: DateTime,
                            endTime: DateTime,
                            costs: Float)

object VisitedViewModel {
  def apply(v: Visited): VisitedViewModel = new VisitedViewModel(
    visitId = v.visitId,
    serviceRequsetId = v.serviceRequsetId,
    scheduleTime = v.scheduleTime,
    outTime = v.outTime.orNull,
    startTime = v.startTime.orNull,
    endTime = v.endTime.orNull,
    costs = v.costs
  )
}

case class VisitedListViewModel(visits: List[VisitedViewModel],
                                totalCost: Double)

object VisitedListViewModel {
  def apply(totalCost: Double, visits: List[Visited]) = new VisitedListViewModel(
    totalCost = totalCost,
    visits = visits.map(VisitedViewModel.apply)
  )
}

class Visiteds @Inject()(json4s: Json4s) extends Controller {

  import json4s._

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  case class VisitCreationForm(requestId: Long,
                               scheduleTime: DateTime,
                               startTime: DateTime,
                               endTime: DateTime,
                               costs: BigDecimal)

  private val visitCreateForm = Form(
    mapping(
      "requestId" -> longNumber,
      "scheduleTime" -> jodaDate,
      "startTime" -> jodaDate,
      "endTime" -> jodaDate,
      "costs" -> bigDecimal
    )(VisitCreationForm.apply)(VisitCreationForm.unapply)
  )

  def all(requestId: Long) = Action {
    val visits = Visited.findAll(requestId)
    val totalCost = Visited.getTotalCost(requestId)
    Ok(Extraction.decompose(VisitedListViewModel(totalCost, visits)))
  }

  def create() = Action { implicit req =>
    visitCreateForm.bindFromRequest().fold(
      formWithErrors => BadRequest(
        formWithErrors.errors
            .foldLeft("")((res, message) =>
              res + message.message + ",")),
      form => {
        Visited.create(form.requestId, form.costs.toFloat,
          form.scheduleTime, startTime = Some(form.startTime),
          endTime = Some(form.endTime))
        Ok
      }
    )
  }
}
