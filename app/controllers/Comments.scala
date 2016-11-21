package controllers

import javax.inject.Inject

import com.github.tototoshi.play2.json4s.native._
import models.Commented
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

class Comments @Inject()(json4s: Json4s) extends Controller {
  import json4s._

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  private val commentForm = Form(
    mapping(
      "personId" -> longNumber,
      "requestId" -> longNumber,
      "text" -> text
    )(CommentForm.apply)(CommentForm.unapply)
  )

  def all(requestId: Long) = Action {
    val comments = Commented.findAll(requestId)
    Ok(Extraction.decompose(comments))
  }

  def create = Action { implicit req =>
    commentForm.bindFromRequest.fold(
      formWithErrors => BadRequest(
        formWithErrors.errors
          .foldLeft("")((res, message) =>
            res + message.message + ",")),
      form => {
        val comment = Commented.create(form.requestId, form.personId, form.text, DateTime.now())
        Ok(Extraction.decompose(comment.id))
      }
    )
  }

  case class CommentForm(personId: Long,
                         requestId: Long,
                         text: String)
}
