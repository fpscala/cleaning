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
import play.api.mvc.{BaseController, ControllerComponents, MultipartFormData, Request}
import protocols.WorkerProtocol.{AddImage, AddWorker, Worker}
import views.html._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                              @Named("worker-manager") val workerManager: ActorRef,
                               indexTemplate: index,
                               workerTemplate: add_worker)
                              (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index = Action {
    Ok(indexTemplate())
  }

  def workerForm = Action {
    Ok(workerTemplate())
  }

  def uploadFile() = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] => {
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
  }}

  def addWorker() = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] => {
    val body = request.body.asFormUrlEncoded
    val surname = body("surname").head
    logger.info(s"surname: $surname")
    val firstName = body("first_name").head
    logger.info(s"firstname: $firstName")
    val lastName = body("last_name").headOption
    val address = body("address").head
    logger.info(s"address: $address")
    val phone = body("phone").head
    logger.info(s"phone: $phone")
    val passportSeriesAndNumber = body("passport_series_and_number").head
    logger.info(s"passportSeriesAndNumber: $passportSeriesAndNumber")
    val dayGettingPassport = parseDate(body("day_getting_passport").head)
    val warnings = body("warnings").headOption.flatMap( w => Option(Json.toJson(w)))
    val pensionNumber = body("pension_number").head.toInt
    logger.info(s"pensionNumber: $pensionNumber")
    val itn = body("itn").head.toInt
    logger.info(s"itn: $itn")
    val genderId = body("genderId").head.toInt
    val birthDay = parseDate(body("birthday").head)
    logger.info(s"birthDay: $birthDay")
    val birthPlace = body("birth_place").head
    logger.info(s"birthPlace: $birthPlace")
    val education = body("educationId").head.toInt

    request.body.file("attachedFile").map { tempFile =>
      val fileName = tempFile.filename
      val imgData = getBytesFromPath(tempFile.ref.path)
      (workerManager ? AddWorker(Worker(None, surname, firstName, lastName, address, phone, passportSeriesAndNumber,
        dayGettingPassport, fileName, imgData, warnings, pensionNumber, itn, genderId, birthDay,
        birthPlace, education))).mapTo[Int].map { _ =>
        Ok(Json.toJson("Successfully uploaded"))
      }
    }.getOrElse(Future.successful(BadRequest("Error occurred. Please try again")))
  }}

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

  private def convertToStrDate(date: Date) = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
  }

  private def parseDateTime(dateStr: String) = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr)
  }

  private def parseDate(dateStr: String) = {
    new SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
  }

}
