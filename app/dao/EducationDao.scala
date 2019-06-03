package dao

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.WorkerProtocol.Education
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

trait EducationComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  class EducationTable(tag: Tag) extends Table[Education](tag, "EducationCodes") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def code = column[String]("code")

    def * = (id.?, name, code) <> (Education.tupled, Education.unapply _)
  }
}

@ImplementedBy(classOf[EducationDaoImpl])
trait EducationDao {
  def addGender(educationData: Education): Future[Int]
}

@Singleton
class EducationDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends EducationDao
    with EducationComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val education = TableQuery[EducationTable]

  override def addGender(educationData: Education): Future[Int] = {
    db.run {
      (education returning education.map(_.id)) += educationData
    }
  }

}

