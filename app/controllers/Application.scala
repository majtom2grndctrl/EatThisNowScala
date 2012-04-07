package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._


object Application extends Controller {

  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  val accountForm = Form(
    tuple(
      "newEmail" -> email,
	  "newFirstName" -> nonEmptyText,
	  "newLastName" -> nonEmptyText,
	  "newPassword" -> tuple(
	    "password1" -> text(minLength = 6),
	    "password2" -> text
	  ).verifying(
	    "Passwords don't match", passwords => passwords._1 == passwords._2
	  )
	)
  )


  def login = Action { implicit request =>
    Ok(html.login(loginForm, accountForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors, accountForm)),
      user => Redirect(routes.Foods.index).withSession("email" -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  } 

  def signUp = Action { implicit request =>
    accountForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(loginForm, formWithErrors)),
      {case (newEmail, newFirstName, newLastName, (password1, password2)) =>
        val userCreate = User.create(
          User(NotAssigned, newEmail, newFirstName, newLastName, password1)
        )
        Redirect(routes.Foods.index).withSession("email" -> newEmail)
      }
    )
  }

  /*
  def javascriptRoutes = Action {
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        
      )
    )
  }
*/


}

trait Secured {
  
  private def username(request: RequestHeader) = request.session.get("email")

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { userEmail: String =>
    Action(request => f(userEmail)(request))
  }

  def IsOwnerOf(food: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { userEmail: String => request =>
    if(Food.isOwner(food, User.findByEmail(userEmail).get.id)) {
      f(userEmail)(request)
    } else {
      Results.Forbidden
    }
  }

}