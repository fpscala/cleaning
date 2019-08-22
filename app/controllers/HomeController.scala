package controllers

import java.nio.file.{Files, Path}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json
import play.api.mvc._
import protocols.OrderProtocol.{AddOrder, AddPrice, Count, GetAllNamesAndPrices, GetCountList, GetPrices, Order, PriceList}
import protocols.WorkerProtocol.{AddImage, AddWorker, Auth, Education, Gender, GetAllEducations, GetAllLoginAndPassword, GetGenderList, Worker}
import views.html._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               @Named("worker-manager") val workerManager: ActorRef,
                               @Named("order-manager") val orderManager: ActorRef,
                               @Named("gender-manager") val genderManager: ActorRef,
                               @Named("education-manager") val educationManager: ActorRef,
                               @Named("authorize-manager") val authorizeManager: ActorRef,
                               indexTemplate: index,
                               orderTemplate: orderList,
                               workerListTemplate: workerList,
                               loginTemplate: loginpage,
                               workerTemplate: add_worker,
                               StatusTemplate: order_status,
                               priceListTemplate: pricelist,
                               addPriceListTemplate: add_pricelist
                              )
                              (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {
  val LoginSessionKey = "login.key"

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index: Action[AnyContent] = Action { implicit request: RequestHeader => {
    Ok(indexTemplate())
  }
  }

  def orderlist: Action[AnyContent] = Action {
    Ok(orderTemplate())
  }

  def workerlist: Action[AnyContent] = Action {
    Ok(workerListTemplate())
  }

  def StatusOrder: Action[AnyContent] = Action {
    Ok(StatusTemplate())
  }

  def showLoginPage: Action[AnyContent] = Action { implicit request: RequestHeader => {
    Ok(loginTemplate())
  }
  }

  case class AllLoginAndPassword(login: String, password: String)

  var userList = List.empty[AllLoginAndPassword]

  def loginPost = Action { implicit request =>
    val formParam = request.body.asFormUrlEncoded
    val login = formParam.get("login").headOption
    val password = formParam.get("password").headOption
    logger.info(s"login: $login, password: $password")
    (authorizeManager ? GetAllLoginAndPassword).mapTo[Seq[Auth]].map { param =>
      param.foreach { login =>
        userList = userList :+ AllLoginAndPassword(login.login, login.password)
        logger.info(s"Users: $userList")
      }
      Ok(Json.toJson(Seq(param)))
    }
    val authByLoginAndPwd = userList.exists(user => user.login == login.getOrElse("") && user.password == password.getOrElse(""))
    if (authByLoginAndPwd) {
      Redirect(routes.HomeController.index()).addingToSession(LoginSessionKey -> login.getOrElse(""))
    } else {
      Redirect(routes.HomeController.showLoginPage()).flashing("error" -> "Your login or password is incorrect.")
    }
  }


  def priceList: Action[AnyContent] = Action {
    Ok(priceListTemplate())
  }

  def addPriceList: Action[AnyContent] = Action { implicit request: RequestHeader => {
    Ok(addPriceListTemplate())
  }
  }


  def workerForm: Action[AnyContent] = Action {
    Ok(workerTemplate())
  }

  def uploadFile(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] => {
    val body = request.body.asFormUrlEncoded
    val name = body.get("name").flatMap(_.headOption)
    logger.info(s"name: $name")
    request.body.file("attachedFile").map { tempFile =>
      val fileName = tempFile.filename
      val imgData = getBytesFromPath(tempFile.ref.path)
      (workerManager ? AddImage(fileName, imgData)).mapTo[Unit].map { _ =>
        Ok(Json.toJson("Successfully uploaded"))
      }
    }.getOrElse(Future.successful(BadRequest("Error occurred. Please try again")))
  }
  }

  def addOrder = Action.async(parse.json) { implicit request => {
    val surname = (request.body \ "surname").as[String]
    val firstName = (request.body \ "firstName").as[String]
    val email = (request.body \ "email").as[String]
    val phone = (request.body \ "phone").as[String]
    val address = (request.body \ "address").as[String]
    val typeCleaning = (request.body \ "typeCleaning").as[String]
    val comment = (request.body \ "comment").as[String]
    val linkCode = randomCode(5)
    val orderDay = new Date
    (orderManager ? AddOrder(Order(None, surname, firstName, address, phone, orderDay, email, comment, linkCode, typeCleaning))).mapTo[Int].map { _ =>
      Ok(Json.toJson(s"$linkCode"))
    }
  }
  }

  private def randomCode(length: Int) = {
    Random.alphanumeric.take(length).mkString.toLowerCase
  }

  def addPrices = {
    Action.async { implicit request =>
      val formParams = request.body.asFormUrlEncoded
      val name = formParams.get("name").head
      val count = formParams.get("count").head
      val price = formParams.get("price").head
      (orderManager ? AddPrice(PriceList(None, name, count, price))).mapTo[Int].map { _ =>
        Redirect(routes.HomeController.addPriceList()).flashing("info" -> "Successfully uploaded.")
      }
    }
  }

  def addWorker(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] => {
    val body = request.body.asFormUrlEncoded
    logger.info(s"body: $body")
    val surname = body("surname").head
    val firstName = body("first_name").head
    val lastName = body("last_name").headOption
    val address = body("address").head
    val phone = body("phone").head
    val passportSeriesAndNumber = body("passport_series_and_number").head
    val dayGettingPassport = parseDate(body("day_getting_passport").head)
    val warnings = body("warnings").headOption.flatMap(w => Option(Json.toJson(w)))
    val pensionNumber = body("pension_number").head.toInt
    val itn = body("itn").head.toLong
    val genderId = body("genderId").head.toInt
    val birthDay = parseDate(body("birthday").head)
    val birthPlace = body("birth_place").head
    val education = body("educationId").head.toInt
    val password = body("password").head

    request.body.file("attachedFile").map { tempFile =>
      val fileName = tempFile.filename
      val imgData = getBytesFromPath(tempFile.ref.path)
      (workerManager ? AddWorker(Worker(None, surname, firstName, lastName, address, phone, passportSeriesAndNumber,
        dayGettingPassport, fileName, imgData, warnings, pensionNumber, itn, genderId, birthDay,
        birthPlace, education, password))).mapTo[Int].map { _ =>
        Ok(Json.toJson("Successfully uploaded"))
      }
    }.getOrElse(Future.successful(BadRequest("Error occurred. Please try again")))
  }
  }


  def getPrices: Action[AnyContent] = Action.async {
    (orderManager ? GetPrices).mapTo[Seq[PriceList]].map { prices =>
      logger.info(s"prices: $prices")
      Ok(Json.toJson(Seq(prices)))
    }
  }

  case class NameAndPrice(name: String, price: String)

  def getNames: Action[AnyContent] = Action.async {
    (orderManager ? GetAllNamesAndPrices).mapTo[Seq[PriceList]].map { names =>
      logger.info(s"names: $names")
      Ok(Json.toJson(Seq(names)))
    }
  }

  def getCounts: Action[AnyContent] = Action.async {
    (genderManager ? GetCountList).mapTo[Seq[Count]].map { prices =>
      logger.info(s"prices: $prices")
      Ok(Json.toJson(Seq(prices)))
    }
  }


  def getGender: Action[AnyContent] = Action.async {
    (genderManager ? GetGenderList).mapTo[Seq[Gender]].map { gender =>
      logger.info(s"genders: $gender")
      Ok(Json.toJson(Seq(gender)))
    }
  }

  def getEducation: Action[AnyContent] = Action.async {
    (educationManager ? GetAllEducations).mapTo[Seq[Education]].map { education =>
      logger.info(s"education: $education")
      Ok(Json.toJson(Seq(education)))
    }
  }

  private def getBytesFromPath(filePath: Path): Array[Byte]
  = {
    Files.readAllBytes(filePath)
  }

  private def convertToStrDate(date: Date)

  = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
  }

  private def parseDateTime(dateStr: String)

  = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr)
  }

  private def parseDate(dateStr: String)

  = {
    new SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
  }

}
