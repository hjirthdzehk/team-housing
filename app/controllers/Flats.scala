package controllers

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.play2.json4s.native._
import models._
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc._

/** Created by a.kiselev on 31/10/2016. */
@Singleton
class Flats @Inject() (json4j: Json4s) extends Controller {
  import json4j._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  def findById(flatId: Int) = Action {
    Ok(Extraction.decompose(
      Flat.findById(flatId))
    )
  }

}
