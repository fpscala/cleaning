package controllers

import java.nio.file.{Files, Path}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents, MultipartFormData, Request}
import protocols.WorkerProtocol.AddImage
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

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

}
