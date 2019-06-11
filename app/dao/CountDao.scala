package dao

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.OrderProtocol.Count
import slick.jdbc.JdbcProfile

import scala.concurrent.Future


trait CountComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  class CountTable(tag: Tag) extends Table[Count](tag, "Counts") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Count.tupled, Count.unapply _)
  }
}

@ImplementedBy(classOf[CountDaoImpl])
trait CountDao {
  def addCount(countData: Count): Future[Int]
  def getCounts(): Future[Seq[Count]]
}

@Singleton
class CountDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends CountDao
    with CountComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val count = TableQuery[CountTable]

  override def addCount(countData: Count): Future[Int] = {
    db.run {
      (count returning count.map(_.id)) += countData
    }
  }

  override def getCounts(): Future[Seq[Count]] = {
    db.run (count.result)
  }

}

