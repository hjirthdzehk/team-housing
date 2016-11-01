package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native.Json4s
import models._
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}


case class MeterReadingData(date: String,
                            value: String)

object MeterReadingData {
  def fromMeterReading(mr: MeterReading) = MeterReadingData(mr.date.toString, mr.value.toString)
}
case class MeterData(title: String,
                     readings: Seq[MeterReadingData],
                     unit: String)

object MeterData {
  def fromMeter(m: Meter) =
    MeterData(
      m.title,
      m.listReadings.map(MeterReadingData.fromMeterReading),
      m.getUnit.description
    )
}

case class ProfileData(personName: String,
                       metersByFlat: Map[String, Seq[MeterData]])


/** Created by a.kiselev on 31/10/2016. */
class Dwellers @Inject() (json4j: Json4s) extends Controller {
  import json4j._
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  def findById(personId: Int) = Action {
    Ok(Extraction.decompose(
      Person.findById(personId))
    )
  }

  def getProfileData(personId: Int) = Action {
    val dweller = Dweller.findById(personId).get
    val person = dweller.getPerson
    val metersByFlat = dweller.listFlats.map(flat => {
      flat.flatNumber.toString -> flat.listMeters
        .map(MeterData.fromMeter)
    }).toMap

    Ok(Extraction.decompose(
      ProfileData(
        s"${person.name} ${person.surname}",
        metersByFlat)))
  }

}
