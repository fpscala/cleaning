package protocols

import java.util.Date

import play.api.libs.json.{Json, OFormat}

object OrderProtocol {

  case class AddOrder(orders: Order)

  case object GetAllOrders

  case object GetPrices

  case object GetAllNamesAndPrices

  case class Order(id: Option[Int] = None,
                   surname: String,
                   firstName: String,
                   address: String,
                   phone: String,
                   orderDay: Date,
                   email: String,
                   comment: String,
                   linkCode: String,
                   type1: String)

  implicit val orderFormat: OFormat[Order] = Json.format[Order]


  case class AddPrice(price: PriceList)

  case object GetAllPrice

  case class PriceList(id: Option[Int] = None,
                       name: String,
                       count: String,
                       price: String)

  implicit val priceListFormat: OFormat[PriceList] = Json.format[PriceList]


  case object GetCountList

  case class Count(id: Option[Int] = None,
                   name: String)

  implicit val countFormat: OFormat[Count] = Json.format[Count]


}
