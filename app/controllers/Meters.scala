package controllers

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.play2.json4s.native._
import models._
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc._

@Singleton
class Meters @Inject() (json4s: Json4s) extends Controller {
  import json4s._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  case class MeterViewModel(id: Integer,
                            title: String,
                            unit: String){

  }
  case class MeterGroup(title: String,
                        meters: List[MeterViewModel]){
  }

  def findByFlatId(flatId: Int) = Action {
    Ok(Extraction.decompose(
      Meter
        .findByFlatId(flatId)
        .groupBy(m => m.`type`)
        .map((titleAndMeters: (String, List[Meter])) =>
          new MeterGroup(titleAndMeters._1,
            titleAndMeters._2
              .map((m: Meter) => new MeterViewModel(m.meterId, m.title, MeterUnit.get(m.meterUnitId).description)))
        )
    ))
  }
}