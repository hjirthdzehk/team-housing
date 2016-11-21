package controllers

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.play2.json4s.native._
import models._
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import play.api.data.Form
import play.api.data.Forms._
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

  case class FlatCreateForm(area: BigDecimal,
                            flatNumber: Int,
                            balance: BigDecimal,
                            cladrId: Int,
                            buildingId: Int)

  val flatCreateForm = Form(
    mapping(
      "area" -> bigDecimal,
      "flatNumber" -> number,
      "balance" -> bigDecimal,
      "cladrId" -> number,
      "buildingId" -> number
    )(FlatCreateForm.apply)(FlatCreateForm.unapply)
  )

  def create() =
    Action {
      implicit req =>
        flatCreateForm.bindFromRequest.fold(
          formWithErrors => BadRequest("Something bad happened during Flat creation"),
          form => {
            val flat = Flat.create(
              form.area,
              form.flatNumber,
              form.balance,
              form.cladrId,
              form.buildingId
            )

            Ok//(Extraction.decompose(flat))
          }
        )
    }
}
