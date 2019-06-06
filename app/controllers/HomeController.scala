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
import play.api.mvc._
import protocols.OrderProtocol.{AddOrder, Order}
import protocols.WorkerProtocol.{AddImage, Education, Gender, GetAllEducations, GetGender}
import views.html._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               @Named("worker-manager") val workerManager: ActorRef,
                               @Named("order-manager") val orderManager: ActorRef,
                               @Named("gender-manager") val genderManager: ActorRef,
                               @Named("education-manager") val educationManager: ActorRef,
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
  }
  }

  addOrder()

  def addOrder(): Action[AnyContent] = Action.async {
    val surname = "Raxmatov"
    val firstName = "Maftunbek"
    val address = "Paxtakor"
    val phone = "+998999673398"
    val orderDay = new Date
    val email = "Prince777_98@mail.ru"
    val comment = "ISHLAGAYSAN"
    val typeName = "price1"
    (orderManager ? AddOrder(Order(None, surname, firstName, address, phone, orderDay, email, comment, typeName))).mapTo[Int].map { _ =>
      Ok(Json.toJson("Successfully uploaded"))
    }
  }



  def getGender() = Action.async{
    (genderManager ? GetGender).mapTo[Seq[Gender]].map { gender =>
      logger.info(s"gender: $gender")
      Ok(Json.toJson(Map("gender" -> gender)))
    }
  }

  def getEducation() = Action.async{
    (educationManager ? GetAllEducations).mapTo[Seq[Education]].map { education =>
      logger.info(s"education: $education")
      Ok(Json.toJson(Map("education" -> education)))
    }
  }

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

}
