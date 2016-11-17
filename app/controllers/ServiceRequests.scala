package controllers

import models.ServiceRequest
import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import org.joda.time.DateTime
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}

/**
  * Created by VladVin on 17.11.2016.
  */
class ServiceRequests @Inject()(json4s: Json4s) extends Controller {
    import json4s._

    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
      def get(requestId: Long) = Action {
          Ok(Extraction.decompose(ServiceRequest.get(requestId)))
      }
//    def all(flatId: Long) = Action {
//        var requests = ServiceRequest.findAllForFlat(flatId)
//        var nextVisits = Seq[DateTime]
//        requests.foreach(sr => nextVisits + ServiceRequest.findNextVisitDate(sr.id))
//        Ok
//    }
}
