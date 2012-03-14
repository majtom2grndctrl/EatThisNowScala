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
				User(Id(1), "dhiester@example.com", "Dan", "Hiester", "secret"),
				User(Id(2), "wbuccicone@example.com", "Whitney", "Buccicone", "secret")
			).foreach(User.create)

			Seq(
				Food(Id(1), "Turkey", false, 1, date("2011-12-20")),
				Food(Id(2), "Ham", false, 2, date("2011-12-22")),
				Food(Id(3), "Pizza", false, 1, date("2011-12-15"))
			).foreach(Food.create)
		}

	}
}
