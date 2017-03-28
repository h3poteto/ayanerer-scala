package com.h3poteto.ayanerer.test

import javax.inject._
import org.scalatestplus.play._
import org.scalatest._
import play.api._
import play.api.test._
import play.api.test.Helpers._
import play.api.inject.guice._
import play.api.db.{ Database, DBApi}
import play.api.db.evolutions.Evolutions

import scala.concurrent.Future

trait DatabaseSpec extends PlaySpec with OneAppPerSuite with BeforeAndAfterEach {
  implicit override lazy val app: Application = new GuiceApplicationBuilder().build

  // Prepare and clean database
  // http://stackoverflow.com/questions/33392905/how-to-apply-manually-evolutions-in-tests-with-slick-and-play-2-4
  lazy val injector = app.injector

  lazy val databaseApi = injector.instanceOf[DBApi] //here is the important line

  override def beforeEach() = {
    Evolutions.applyEvolutions(databaseApi.database("default"))
  }

  override def afterEach() = {
    Evolutions.cleanupEvolutions(databaseApi.database("default"))
  }
}

