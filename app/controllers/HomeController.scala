package controllers

import javax.inject._
import play.api.mvc.{BaseController, ControllerComponents}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
