package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import anorm._

import java.util.{Date}

class ModelSpec extends Specification {

  import models._

  // -- Date helper
  def date(str: String) = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(str)

  "Food model" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(mashedPotatoes) = Food.findById(1000)

        mashedPotatoes.name must equalTo("Mashed Potatoes")
        mashedPotatoes.eaten must equalTo(false)
        mashedPotatoes.expiry must equalTo(date("2012/05/21"))
        mashedPotatoes.id must equalTo(Id(1000))
        mashedPotatoes.owner must equalTo(Id(1))
      }
    }

    "Add new food to test user's list" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
	
        Food.create(Food(NotAssigned, "Test Food", false, Id(1), date("11/19/2012")))
	
        val Some(testFood) = Food.findById(1005)
	
        testFood.name must equalTo("Test Food")
        testFood.eaten must equalTo(false)
        testFood.owner must equalTo(Id(1))
        testFood.expiry must equalTo(date("11/19/2012"))
      }
    }

    "Mark food as eaten" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
	
        Food.markAsEaten(1000, true)

        val Some(testFood) = Food.findById(1000)
	
        testFood.eaten must equalTo(true)
      }
    }
  }

  "User model" should {
    "retrieve user by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(testUser) = User.findById(1: Long)

        testUser.firstName must equalTo("Test")
        testUser.lastName must equalTo("User")
        testUser.email must equalTo("test@example.com")
        testUser.password must equalTo("secret")
        testUser.id must equalTo(Id(1))
      }
    }

    "retreive all users" in {
      running(FakeApplication()) {

        val users: Seq[User] = User.findAll

        users must equalTo(
          List(
            User(Id(1),"test@example.com","Test","User","secret"),
            User(Id(2),"dhiester@example.com","Dan","Hiester","secret"),
            User(Id(3),"wbuccicone@example.com","Whitney","Buccicone","secret")
          )
        )
      }
    }

  }
}