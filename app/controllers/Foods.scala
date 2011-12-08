package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import java.util.{Date}

import anorm._

import models._
import views._

object Foods extends Controller with Secured {

/*  def index = Action { request =>
    Ok(
        Food.sadacheTest(request.username.get)
    )
  }
*/
  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
//      Food.sadacheTest(username)
      Ok(
        html.foods.index(
          Food.findFoodFor(username),
          user
        )
      )
    }.getOrElse(Forbidden)
  }

  def markAsEaten(food: Long) = IsOwnerOf(food) { _ => implicit request =>
    Food.markAsEaten(food, true: Boolean)
    Ok
    Redirect(routes.Foods.index)
  }
}
