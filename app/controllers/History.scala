package controllers

import javax.inject.Singleton

import com.github.tototoshi.play2.json4s.native.Json4s
import com.google.inject.Inject
import models.{Flat, Meter, MeterReading, MeterUnit}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}

/**
  * Created by VladVin on 02.11.2016.
  */

case class MeterReadingEntry(value: Double,
                             date: String,
                             paid: Boolean)

case class MeterHistory(meterTitle: String,
                       meterUnitTitle: String,
                       meterReadings: Seq[MeterReadingEntry])

object MeterHistory {
    def fromMeter(meter: Meter): MeterHistory = new MeterHistory(
        meter.title,
        meter.getUnit.description,
        meter.listReadings
            .map((mr: MeterReading) => MeterReadingEntry(
                mr.value.toDouble,
                mr.date.toString(),
                mr.paid
            ))
    )
}

@Singleton
class History @Inject() (json4s: Json4s) extends Controller {
    import json4s._
    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

    def getMetersHistory(flatId: Int) = Action {
        var historyItems = Seq[MeterHistory]()

        Flat.findById(flatId)
            .foreach((f: Flat) => {
                f.listMeters
                    .foreach((m: Meter) => {
                        historyItems +:= MeterHistory.fromMeter(m)
                    })
            })

        Ok(Extraction.decompose(historyItems))
    }
}
