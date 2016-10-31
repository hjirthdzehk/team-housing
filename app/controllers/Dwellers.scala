package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native.Json4s
import models.Person
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}

/** Created by a.kiselev on 31/10/2016. */
class Dwellers @Inject() (json4j: Json4s) extends Controller {
  import json4j._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  def findById(personId: Int) = Action {
    Ok(Extraction.decompose(
      Person.findById(personId))
    )
  }

}
