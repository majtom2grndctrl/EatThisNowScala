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
/*
  val foodForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "expiry" -> date("MM/dd/yyyy")
    )
  )
*/

  def foodForm(implicit request: RequestHeader) = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText,
      "status" -> ignored("edible": String),
      "owner" -> ignored(request.session.get("user.id").asInstanceOf[Pk[Long]]),
      "expiry" -> date("MM/dd/yyyy")
    )(Food.apply)(Food.unapply)
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
        html.foods.index(
          user,
          Food.findEdibleFoodFor(user.id),
          foodForm
        )
      )
    }.getOrElse(Forbidden)
  }

  def loadFood = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
	    html.foods.loadListItems(
	      Food.findEdibleFoodFor(user.id)
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

  def markAsEaten(foodId: Long) = IsOwnerOf(foodId) { _ => implicit request =>
    Food.updateStatus(foodId, "eaten")
    Ok
    Redirect(routes.Foods.index)
  }

  def saveFood = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      foodForm.bindFromRequest.fold(
        errors => BadRequest(html.foods.create(errors, user)),
        Food.create(food)
        Ok
        Redirect(routes.Foods.index)
      )
    }.getOrElse(Forbidden)
  }

}
