package actors

import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.util.Date

import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.WorkerProtocol.{AddImage, AddWorker, Worker}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class WorkerManager @Inject()(val environment: Environment,
                              val configuration: Configuration,
//                              workersDao: WorkersDao
                             )
                             (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)
  val config: Configuration = configuration.get[Configuration]("server")
  val imagesPath: String = config.get[String]("images-files")
  val imagesDir: Path = Paths.get(imagesPath)

  def receive = {
    case AddWorker(worker) =>
      addWorker(worker).pipeTo(sender())

    case AddImage(fileName, imgData) =>
      addImage(fileName, imgData).pipeTo(sender())

    case _ => logger.info(s"received unknown message")
  }

  private def addWorker(worker: Worker): Future[Int] = {
    Future.successful(1)
  }

  def addImage(fileName:String, imgData: Array[Byte]): Future[Unit]  = {
    Future {
      Files.write(imagesDir.resolve(new Date().getTime.toString + ".png"), imgData)
    }
  }

//  def getImage(fileId: String) = {
//    Future {
//      require(isCorrectFileName(fileId))
//      Files.readAllBytes(imagesDir.resolve(fileId + ".png"))
//    }
//  }
//
//  def getImages = {
//    Future {
//      new File(imagesDir.toString)
//        .listFiles
//        .filter(_.isFile)
//        .map(_.getName.split('.')(0))
//        .toList
//    }
//  }
//  def isCorrectFileName(name: String) = {
//    badCharsR.findFirstIn(name).isEmpty
//  }
//
//  private val badCharsR = """\/|\.\.|\?|\*|:|\\""".r // / .. ? * : \
}
