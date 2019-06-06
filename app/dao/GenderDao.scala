package dao

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.WorkerProtocol.Gender
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

trait GenderComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  class GenderTable(tag: Tag) extends Table[Gender](tag, "GenderCodes") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def code = column[String]("code")

    def * = (id.?, name, code) <> (Gender.tupled, Gender.unapply _)
  }
}

@ImplementedBy(classOf[GenderDaoImpl])
trait GenderDao {
  def addGender(genderData: Gender): Future[Int]
  def getGenders(): Future[Seq[Gender]]
}

@Singleton
class GenderDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends GenderDao
    with GenderComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val gender = TableQuery[GenderTable]

  override def addGender(genderData: Gender): Future[Int] = {
    db.run {
      (gender returning gender.map(_.id)) += genderData
    }
  }
  override def getGenders(): Future[Seq[Gender]] = {
    db.run (gender.result)
  }

}

