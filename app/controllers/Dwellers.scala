package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native.Json4s
import models.Person._
import models._
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.ext.JodaTimeSerializers
import play.api.mvc.{Action, Controller}
import scalikejdbc._

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

  def getProfileData(personId: Int)(implicit session: DBSession = autoSession) = Action {
    val (d, p, f, m, df) = (Dweller.d, Person.p, Flat.f, Meter.m, DwellerLivesInFlat.df)
    val query = sql"""select ${d.result.*}, ${p.result.*}, ${f.result.*}, ${m.result.*}
          from ${Dweller as d} natural join ${Person as p}
            natural join ${DwellerLivesInFlat as df}
            natural join ${Flat as f}
            natural join ${Meter as m}
          where ${d.personId} = ${personId}
       """.map(rs => {
      (Person(p)(rs), Flat(f)(rs), Meter(m)(rs))
    }).list().apply()

    Ok(Extraction.decompose(
      query.groupBy(_._1)
        .map{ case (person, tail) => {
          ProfileData(
            s"${person.name} ${person.surname}",
            tail.groupBy(_._2)
              .map{ case (flat, tail) =>
                flat.flatNumber.toString ->
                  tail.filter(_._2 == flat).map(_._3).map(MeterData.fromMeter)
              }
          )
        }}.head))
  }

}
