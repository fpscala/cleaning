package actors

import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import play.api.Environment
import protocols.WorkerProtocol.{AddWorker, Worker}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class WorkerManager @Inject()(val environment: Environment,
//                              workersDao: WorkersDao
                             )
                             (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {

  implicit val defaultTimeout = Timeout(60.seconds)

  def receive = {
    case AddWorker(worker) =>
      addWorker(worker).pipeTo(sender())

    case _ => logger.info(s"received unknown message")
  }

  private def addWorker(worker: Worker): Future[Int] = {
    Future.successful(1)
  }
}
