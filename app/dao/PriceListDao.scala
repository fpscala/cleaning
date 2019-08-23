package dao

import javax.inject.{Inject, Singleton}
import com.google.inject.ImplementedBy
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import com.typesafe.scalalogging.LazyLogging
import slick.jdbc.JdbcProfile
import protocols.OrderProtocol._


import scala.concurrent.Future

trait PriceListComponent extends CountComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  val countTable = TableQuery[CountTable]

  class PriceListTable(tag: Tag) extends Table[PriceList](tag, "Price_list") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def count = column[String]("count")

    def price = column[String]("price")

    def title = column[String]("title")

    def * = (id.?, name, count, price, title) <> (PriceList.tupled, PriceList.unapply _)

    def getCount = foreignKey("Price_listFkCountName", count, countTable)(_.name)

  }

}

@ImplementedBy(classOf[PriceListDaoImpl])
trait PriceListDao {
  def create(priceData: PriceList): Future[Int]

  def getPrices: Future[Seq[PriceList]]

}

@Singleton
class PriceListDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends PriceListDao
    with PriceListComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val price = TableQuery[PriceListTable]

  override def create(priceData: PriceList): Future[Int] = {
    db.run {
      (price returning price.map(_.id)) += priceData
    }
  }

  override def getPrices(): Future[Seq[PriceList]] = {
    db.run(price.result)
  }
}

