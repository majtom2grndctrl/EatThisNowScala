package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import java.util.{Date}

import anorm._

import views._
import models._

object Foods extends Controller with Secured {

  val Home = Redirect(routes.Foods.index())

  val foodForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "expiry" -> date("MM/dd/yyyy")
    )
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
        html.foods.index(
          user,
          foodForm
        )
      )
    }.getOrElse(Forbidden)
  }

  def loadFood = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
	    html.foods.load(
	      Food.findFoodFor(user.id),
	      user
	    )
      )
    }.getOrElse(Forbidden)
  }

  def createFood = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
        html.foods.create(
          foodForm,
          user
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

  def saveFood = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      foodForm.bindFromRequest.fold(
        errors => BadRequest(html.foods.create(errors, user)),
        {case (name, expiry) => 
          val food = Food.create(
            Food(NotAssigned, name, false, user.id, expiry)
          )
          Ok
          Redirect(routes.Foods.index)
        }
      )
    }.getOrElse(Forbidden)
  }

}
