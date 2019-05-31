package protocols

import java.util.Date

import play.api.libs.json.Json

object OrderProtocol {
    case class AddOrder(orders: Order)
    case object GetAllOrders
    case class Order(surname: String,
                      firstName: String,
                      address: String,
                      phone: String,
                      orderDay: String,
                      email: String,
                      comment: String)

  implicit val orderFormat = Json.format[Order]

}
