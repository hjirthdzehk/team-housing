package controllers

import javax.inject.Singleton

import com.github.tototoshi.play2.json4s.native.Json4s
import com.google.inject.Inject
import models.{Flat, Meter, MeterReading}
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.mvc.{Action, Controller}

/**
  * Created by VladVin on 02.11.2016.
  */

case class MeterReadingEntry(value: Double,
                             date: String,
                             paid: Boolean)

object MeterReadingEntry {
  def fromReading(mr: MeterReading) =
    MeterReadingEntry(
      value = mr.value.toDouble,
      date = mr.date.toString(),
      paid = mr.paid)
}

case class MeterHistory(meterTitle: String,
                        meterUnitTitle: String,
                        meterReadings: Seq[MeterReadingEntry])

object MeterHistory {
  def fromMeter(meter: Meter): MeterHistory = new MeterHistory(
    meter.title,
    meter.getUnit.description,
    meter.listReadings
      .map(MeterReadingEntry.fromReading)
  )
}

@Singleton
class History @Inject() (json4s: Json4s) extends Controller {

  import json4s._

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  def getMetersHistory(flatId: Int) = Action {
    val historyItems = Flat.findById(flatId).toList.flatMap { f =>
      f.listMeters.map(MeterHistory.fromMeter)
    }
    Ok(Extraction.decompose(historyItems))
  }
}
