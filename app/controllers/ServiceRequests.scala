package controllers

import models.ServiceRequest
import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import org.joda.time.DateTime
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}

case class ServiceRequestViewModel(
                                    id: Long,
                                    description: String,
                                    rating: Option[Int],
                                    status: Option[Int],
                                    nextVisitDate: Option[DateTime]
                                  )

class ServiceRequests @Inject()(json4s: Json4s) extends Controller {
    import json4s._

    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
      def get(requestId: Long) = Action {
          val serviceRequest = ServiceRequest.get(requestId)
          val nextVisitDate = ServiceRequest.findNextVisitDate(requestId)
          Ok(Extraction.decompose(ServiceRequestViewModel(
            id=serviceRequest.id,
            description = serviceRequest.description,
            rating = serviceRequest.rating,
            status = serviceRequest.status,
            nextVisitDate = nextVisitDate
          )))
      }
//    def all(flatId: Long) = Action {
//        var requests = ServiceRequest.findAllForFlat(flatId)
//        var nextVisits = Seq[DateTime]
//        requests.foreach(sr => nextVisits + ServiceRequest.findNextVisitDate(sr.id))
//        Ok
//    }
}
