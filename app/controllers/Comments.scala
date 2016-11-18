package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import models.{Commented, Visited}
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.mvc.{Action, Controller}

class Comments @Inject()(json4s: Json4s) extends Controller {
    import json4s._

    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

    def all(requestId: Long) = Action {
      val comments = Commented.findAll(requestId)
        Ok(Extraction.decompose(comments))
    }
}
