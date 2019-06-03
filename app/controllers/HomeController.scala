package controllers

import java.nio.file.{Files, Path}
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
                               indexTemplate: index)
                              (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index = Action {
    Ok(indexTemplate())
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
    val surname = body.get("surname").map(_.head).getOrElse("none")
    val fristName = body.get("frist_name").map(_.head).getOrElse("none")
    val lastName = body.get("last_name").map(_.head)
    val address = body.get("address").map(_.head).getOrElse("none")
    val phone = body.get("phone").map(_.head).getOrElse("none")
    val passportSeriesAndNumber = body.get("passport_series_and_number").map(_.head).getOrElse("none")
//    val dayGettingPassport = body.get("day_getting_passport").flatMap(_.headOption)
    val dayGettingPassport = new Date
    val photoName = body.get("photo_name").map(_.head).getOrElse("none")
    val warnings = body.get("warnings").map(_.head)
    val pensionNumber = body.get("pension_number").map(_.head.toInt).getOrElse(0)
    val itn = body.get("itn").map(_.head.toInt).getOrElse(0)
    val genderId = 1
//    val birthDay = body.get("birthday").flatMap(_.headOption)
    val birthDay = new Date
    val birthPlace = body.get("birth_place").map(_.head).getOrElse("none")
    val education = 1

    request.body.file("attachedFile").map { tempFile =>
      val fileName = tempFile.filename
      val imgData = getBytesFromPath(tempFile.ref.path)
      (workerManager ? AddWorker(Worker(None, surname, fristName, lastName, address, phone, passportSeriesAndNumber,
        dayGettingPassport, fileName, imgData, Option(Json.toJson(warnings)), pensionNumber, itn, genderId, birthDay,
        birthPlace, education))).mapTo[Unit].map { _ =>
        Ok(Json.toJson("Successfully uploaded"))
      }
    }.getOrElse(Future.successful(BadRequest("Error occurred. Please try again")))
  }}

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

}
