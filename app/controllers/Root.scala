package controllers

import play.api.mvc._

class Root extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def login = Action {
    Ok(views.html.login())
  }
}
