package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native.Json4s
import models._
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc._

class AbnormalConsumers @Inject()(json4s: Json4s) extends Controller{
  import json4s._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  def get() = Action{
    Ok(Extraction.decompose(AbnormalConsumer.abnormalConsumers))
  }
}