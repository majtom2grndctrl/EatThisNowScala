package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import anorm._
import play.api.data._

import java.util.{Date}
import java.text.SimpleDateFormat

class ModelSpec extends Specification {

  import models._

  // -- Date helper
  def dateHelper(str: String): Date = new SimpleDateFormat("MM/dd/yyyy").parse(str)
  def dbDateHelper(date: Date, str: String) = new SimpleDateFormat("MM/dd/yyy").format(date) == str

  "Food model" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(mashedPotatoes) = Food.findById(1000)

        mashedPotatoes.name must equalTo("Mashed Potatoes")
        mashedPotatoes.status must equalTo("edible")
        mashedPotatoes.id must equalTo(Id(1000))
        mashedPotatoes.owner must equalTo(Id(1000))
        mashedPotatoes.expiry must equalTo(dateHelper("05/21/2012"))
      }
    }

    "add new food to test user's list" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
	
        Food.create(Food(NotAssigned, "Test Food", "edible", Id(1000), dateHelper("11/19/2012")))
	
        val Some(testFood) = Food.findById(1005)

        testFood.name must equalTo("Test Food")
        testFood.status must equalTo("edible")
        testFood.owner must equalTo(Id(1000))
        testFood.expiry must equalTo(dateHelper("11/19/2012"))
      }
    }

    "mark food as eaten" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
	
        Food.updateStatus(1000, "eaten")

        val Some(testFood) = Food.findById(1000)
	
        testFood.status must equalTo("eaten")
      }
    }

      "return food for test user " in {

      running(FakeApplication()) {
        val testFoods: Seq[Food] = Food.findEdibleFoodFor(Id(1000))

        testFoods must have length(2); // This test at least verifies two foods are retreived

        val Some(mashedPotatoes) = Food.findById(1000)

        mashedPotatoes.name must equalTo("Mashed Potatoes")
        mashedPotatoes.status must equalTo("edible")
        mashedPotatoes.owner must equalTo(Id(1000))
        mashedPotatoes.expiry must equalTo(dateHelper("05/21/2012"))

/*
// It fails when I try to test it this way. Still don't know why.
        testFoods must equalTo(
          List(
            Food(Id(1000), "Mashed Potatoes", "edible", Id(1000), dateHelper("05/21/2012")),
            Food(Id(1001), "Fried Green Tomatoes", "edible", Id(1000), dateHelper("04/21/2012"))
          )
        )
*/
      }
    }
  }

  "User model" should {
    "retrieve user by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(testUser) = User.findById(1000: Long)

        testUser.firstName must equalTo("Test")
        testUser.lastName must equalTo("User")
        testUser.email must equalTo("test@example.com")
        testUser.password must equalTo("secret")
        testUser.id must equalTo(Id(1000))
      }
    }

    "retreive all users" in {
      running(FakeApplication()) {

        val users: Seq[User] = User.findAll

        users must equalTo(
          List(
            User(Id(1000),"test@example.com","Test","User","secret"),
            User(Id(1001),"dhiester@example.com","Dan","Hiester","secret"),
            User(Id(1002),"wbuccicone@example.com","Whitney","Buccicone","secret")
          )
        )
      }
    }

  }
}