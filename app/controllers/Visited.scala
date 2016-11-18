package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import models.{ServiceRequest, Visited}
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.mvc.{Action, Controller}

/**
  * Created by VladVin on 17.11.2016.
  */
class Visiteds @Inject()(json4s: Json4s) extends Controller {
    import json4s._

    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
    def all(requestId: Long) = Action {
        var visits = Visited.findAll(requestId)
        Ok(Extraction.decompose(visits))
    }
}
