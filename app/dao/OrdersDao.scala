package dao

import java.util.Date

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.OrderProtocol.Order
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.Future

trait OrdersComponent extends PriceListComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  val PriceListTable = TableQuery[PriceListTable]

  class Orders(tag: Tag) extends Table[Order](tag, "Orders") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def surname = column[String]("surname")

    def firstName = column[String]("firstName")

    def address = column[String]("address")

    def phone = column[String]("phone")

    def orderDay = column[Date]("orderDay")

    def email = column[String]("email")

    def comment = column[String]("comment")

    def linkCode = column[String]("linkCode")

    def typeName = column[String]("type")

    def price = column[String]("price")

    def statusOrder = column[Int]("status_order")

    def * = (id.?, surname, firstName, address, phone, orderDay, email, comment, linkCode, typeName, price, statusOrder) <> (Order.tupled, Order.unapply _)

    def type1 = foreignKey("OrdersFkPrice_listName", typeName, PriceListTable)(_.name)
  }

}

@ImplementedBy(classOf[OrdersDaoImpl])
trait OrdersDao {
  def create(ordersData: Order): Future[String]

  def update(order: Order): Future[Int]

  def getOrders: Future[Seq[Order]]

  def getOrderById(id: Int): Future[Option[Order]]

  def getOrderByLinkCode(linkCode: String): Future[Option[Order]]

  def findOrderByPhone(phone: String, type1: String): Future[Option[Order]]
}

@Singleton
class OrdersDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends OrdersDao
    with OrdersComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val orders = TableQuery[Orders]

  override def create(orderData: Order): Future[String] = {
    db.run {
      (orders returning orders.map(_.linkCode)) += orderData
    }
  }

  override def update(order: Order): Future[Int] = {
    db.run {
      orders.filter(_.id === order.id).update(order)
    }
  }

  override def getOrders(): Future[Seq[Order]] = {
    db.run(orders.result)
  }

  override def getOrderById(id: Int): Future[Option[Order]] = {
    db.run(orders.filter(_.id === id).result.headOption)
  }

  override def findOrderByPhone(phone: String, type1: String): Future[Option[Order]] = {
    db.run(orders.filter(order => order.phone === phone && order.typeName === type1).result.headOption)
  }

  override def getOrderByLinkCode(linkCode: String): Future[Option[Order]] = {
    db.run(orders.filter(_.linkCode === linkCode).result.headOption)
  }
}