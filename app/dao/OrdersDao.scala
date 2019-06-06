package dao

import javax.inject.{Inject, Singleton}
import com.google.inject.ImplementedBy
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import com.typesafe.scalalogging.LazyLogging
import protocols.OrderProtocol.Order
import slick.jdbc.JdbcProfile
import slick.model.ForeignKeyAction

import scala.concurrent.Future

trait OrdersComponent extends PriceListComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  val PriceListTable = TableQuery[PriceListTable]

  class Orders(tag: Tag) extends Table[Order](tag, "Orders") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def surname = column[String]("surname")
    def firstName = column[String]("firstName")
    def address  = column[String]("address")
    def phone = column[String]("phone")
    def orderDay = column[String]("orderDay")
    def email  = column[String]("email")
    def comment  = column[String]("comment")
    def typeName = column[String]("type")
    def * = (id.?, surname, firstName, phone, address, orderDay, address, comment, typeName) <> (Order.tupled, Order.unapply _)
    def type1 = foreignKey("OrdersFkPrice_listName", typeName, PriceListTable)(_.name)
  }
}

@ImplementedBy(classOf[OrdersDaoImpl])
trait OrdersDao {
  def create(ordersData: Order): Future[Int]
  def getOrders: Future[Seq[Order]]
}

@Singleton
class OrdersDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends OrdersDao
    with OrdersComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val orders = TableQuery[Orders]

  override def create(orderData: Order): Future[Int] = {
    db.run {
      (orders returning orders.map(_.id)) += orderData
    }
  }

  override def getOrders(): Future[Seq[Order]] = {
    db.run(orders.result)
  }
}