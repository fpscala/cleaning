package dao

import java.util.Date

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import protocols.WorkerProtocol._
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.Future

trait WorkerComponent extends GenderComponent with EducationComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  val genderTable = TableQuery[GenderTable]
  val educationTable = TableQuery[EducationTable]

  class WorkerTable(tag: Tag) extends Table[Worker](tag, "Workers") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def surname = column[String]("surname")
    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def address = column[String]("address")
    def phone = column[String]("phone")
    def passportSeriesAndNumber = column[String]("passportSeriesAndNumber")
    def dayGettingPassport = column[Date]("dayGettingPassport")
    def photoName = column[String]("photoName")
    def photoHash = column[Array[Byte]]("photoHash")
    def warnings = column[JsValue]("warnings")
    def pensionNumber = column[Int]("pensionNumber")
    def itn = column[Int]("itn")
    def genderId = column[Int]("gender")
    def birthDay = column[Date]("birthDay")
    def birthPlace = column[String]("birthPlace")
    def educationId = column[Int]("education")

    def * = (id.?, surname, firstName, lastName.?, address, phone, passportSeriesAndNumber,
      dayGettingPassport, photoName, photoHash, warnings.?, pensionNumber, itn, genderId, birthDay, birthPlace,
      educationId) <> (Worker.tupled, Worker.unapply _)

    def gender = foreignKey("workersFkGenderCode", genderId, genderTable)(_.id)
    def education = foreignKey("workersFkEducationCode", educationId, educationTable)(_.id)
  }
}

@ImplementedBy(classOf[WorkerDaoImpl])
trait WorkersDao {
  def addWorker(workerData: Worker): Future[Int]
  def updateWorker(workerData: Worker): Future[Int]
  def deleteWorker(students: String): Future[Int]
  def getWorker: Future[Seq[Worker]]
}

@Singleton
class WorkerDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends WorkersDao
    with WorkerComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val worker = TableQuery[WorkerTable]

  override def addWorker(workerData: Worker): Future[Int] = {
    db.run {
      (worker returning worker.map(_.id)) += workerData
    }
  }

  override def updateWorker(workerData: Worker): Future[Int] = {
    db.run {
      worker.filter(_.surname === workerData.surname).update(workerData)
    }
  }

  override def deleteWorker(workerName: String): Future[Int] = {
    db.run(worker.filter(_.firstName === workerName).delete)
  }

  override def getWorker(): Future[Seq[Worker]] = {
    db.run(worker.result)
  }
}

