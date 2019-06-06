package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.OrdersDao
import javax.inject.Inject
import play.api.Environment
import protocols.OrderProtocol.{AddOrder, GetAllOrders, Order}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class OrderManager @Inject()(val environment: Environment,
                             orderDao: OrdersDao)
                            (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout = Timeout(60.seconds)

  def receive = {
    case AddOrder(order) =>
      addOrder(order).pipeTo(sender())

    case GetAllOrders =>
      getAllOrder.pipeTo(sender())

    case _ => log.info(s"received unknown message")
  }

  private def addOrder(orderData: Order) = {
    orderDao.create(orderData).flatMap { order =>
      Future.successful(order)
    }
  }

  private def getAllOrder = {
    orderDao.getOrders
  }
}