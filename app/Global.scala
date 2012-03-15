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
 * in the sample application.
 */

object InitialData {

	def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

	def insert() = {
		if(User.findAll.isEmpty) {
			Seq(
				User(NotAssigned, "dhiester@example.com", "Dan", "Hiester", "secret"),
				User(NotAssigned, "wbuccicone@example.com", "Whitney", "Buccicone", "secret")
			).foreach(User.create)

			Seq(
				Food(NotAssigned, "Turkey", false, Id(1), date("2011-12-20")),
				Food(NotAssigned, "Ham", false, Id(2), date("2011-12-22")),
				Food(NotAssigned, "Pizza", false, Id(1), date("2011-12-15"))
			).foreach(Food.create)
		}

	}
}
