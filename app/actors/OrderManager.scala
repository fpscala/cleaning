package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.{OrdersDao, PriceListDao}
import javax.inject.Inject
import play.api.Environment
import protocols.OrderProtocol
import protocols.OrderProtocol.{AddOrder, AddPrice, GetAllOrders, GetPrices, Order, PriceList}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class OrderManager @Inject()(val environment: Environment,
                             orderDao: OrdersDao,
                             priceListDao: PriceListDao)
                            (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout = Timeout(60.seconds)

  def receive = {
    case AddOrder(order) =>
      addOrder(order).pipeTo(sender())

    case AddPrice(price) =>
      addPrice(price).pipeTo(sender())

    case GetAllOrders =>
      getAllOrder.pipeTo(sender())

    case GetPrices =>
      getPrices.pipeTo(sender())


    case _ => log.info(s"received unknown message")
  }

  private def addOrder(orderData: Order): Future[Int] = {
    orderDao.create(orderData).flatMap { order =>
      Future.successful(order)
    }
  }

  private def addPrice(priceData: PriceList): Future[Int] = {
    priceListDao.create(priceData).flatMap { price =>
      Future.successful(price)
    }
  }

  private def getAllOrder: Future[Seq[OrderProtocol.Order]] = {
    orderDao.getOrders
  }

  private def getPrices: Future[Seq[OrderProtocol.PriceList]] = {
    priceListDao.getPrices
  }
}