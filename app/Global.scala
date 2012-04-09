import play.api._
import play.api.db._

import models._
import anorm._
import anorm.SqlParser._

object Global extends GlobalSettings {
	override def onStart(app: Application) {
		InitialData.insert()

	}
}

/**
 * Initial set of data to be imported 
 * into the application.
 */

object InitialData {

  
	def dateHelper(str: String): java.util.Date = new java.text.SimpleDateFormat("MM/dd/yyyy").parse(str)

	def insert() = {
		if(User.findAll.isEmpty) {
			Seq(
			    User(NotAssigned, "test@example.com", "Test", "User", "secret"),
				User(NotAssigned, "dhiester@example.com", "Dan", "Hiester", "secret"),
				User(NotAssigned, "wbuccicone@example.com", "Whitney", "Buccicone", "secret")
			).foreach(User.create)

			Seq(
			    Food(NotAssigned, "Mashed Potatoes", "edible", Id(1), dateHelper("05/21/2012")),
			    Food(NotAssigned, "Fried Green Tomatoes", "edible", Id(1), dateHelper("04/21/2012")),
				Food(NotAssigned, "Turkey", "edible", Id(2), dateHelper("12/20/2011")),
				Food(NotAssigned, "Ham", "edible", Id(3), dateHelper("12/22/2011")),
				Food(NotAssigned, "Pizza", "edible", Id(2), dateHelper("12/15/2011"))
			).foreach(Food.create)
		}

	}
}
