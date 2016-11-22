package controllers

import play.api.mvc._

class Root extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def admin = Action {
    Ok(views.html.admin())
  }

  def login = Action {
    Ok(views.html.login())
  }

  def register = Action {
    Ok(views.html.register())
  }
}
