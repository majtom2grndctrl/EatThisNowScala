package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation.Constraints._

import java.util.{Date}

import anorm._

import views._
import models._

object Foods extends Controller with Secured {

  val foodForm = Form(
    of(Food.apply _, Food.unapply _)(
      "id" -> ignored(NotAssigned),
      "name" -> requiredText,
      "eaten" -> boolean,
      "owner" -> requiredText,
      "expiry" -> date("MM-dd-yyyy")
    )
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
//      Food.sadacheTest(username)
      Ok(
        html.foods.index(
          Food.findFoodFor(username),
          user,
          foodForm
        )
      )
    }.getOrElse(Forbidden)
  }

  def markAsEaten(food: Long) = IsOwnerOf(food) { _ => implicit request =>
  	println("markAsEaten " + food)
    Food.markAsEaten(food, true: Boolean)
    Ok
    Redirect(routes.Foods.index)
  }
/*
  def save = Action { implicit request =>
    foodForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.foods.index(formWithErrors)),
      food => {
        Food.insert(food)
        Home.flashing("success" -> "Food %s has been created".format(food.name))
      }
    )
  }
*/
}
