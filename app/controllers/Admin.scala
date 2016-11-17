package controllers

import play.api.mvc._

class Admin extends Controller {
  def index = Action {
    Ok(views.html.admin())
  }
}
