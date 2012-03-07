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
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "name" -> nonEmptyText,
      "eaten" -> boolean,
      "owner" -> nonEmptyText,
      "expiry" -> date("MM-dd-yyyy")
    )(Food.apply)(Food.unapply)
  )

  val newFoodForm = Form(
    tuple(
      "name" -> nonEmptyText,
//      "expiry" -> text.verifying(pattern("""^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\d\d$""".r))
      "expiry" -> date("MM-dd-yyyy")
    )
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
//      Food.sadacheTest(username)
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
	      Food.findFoodFor(username),
	      user
	    )
      )
    }.getOrElse(Forbidden)
  }

  //Not really used right now, might be used later.
/*  def create = IsAuthenticated { username => _ =>
  	User.findByEmail(username).map { user =>
  		Ok(
  		    html.foods.createFood(
  		      foodForm,
  		      user
  		    )
  		)
  	}.getOrElse(Forbidden)
  }
*/
  def createFood = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Ok(
        html.foods.createFood(
          newFoodForm,
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

  def save = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
	    foodForm.bindFromRequest.fold(
	      formWithErrors => BadRequest(html.foods.create(formWithErrors, user)),
	      food => {
	        Food.create(food)
	        Home.flashing("success" -> "Food %s has been created".format(food.name))
	      }
	    )

    }.getOrElse(Forbidden)
  }

  def saveFood = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      
      newFoodForm.bindFromRequest.fold(
        errors => BadRequest(html.foods.createFood(errors, user)),
        {case (name, expiry) => 
          val food = Food.create(
            Food(NotAssigned, name, false, user.email, expiry.toDate())
          )
          Ok(controllers.Foods.createFood(food))
        }
      )
    }.getOrElse(Forbidden)
  }

}
