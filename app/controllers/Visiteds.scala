package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import models.{ServiceRequest, Visited}
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.mvc.{Action, Controller}

case class VisitedViewModel(
                           totalCost: Double,
                           visits: List[Visited]
                           )

class Visiteds @Inject()(json4s: Json4s) extends Controller {
    import json4s._

    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
    def all(requestId: Long) = Action {
        val visits = Visited.findAll(requestId)
        val totalCost = Visited.getTotalCost(requestId)
        Ok(Extraction.decompose(VisitedViewModel(totalCost, visits)))
    }
}
