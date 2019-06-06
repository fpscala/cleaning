package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.EducationDao
import javax.inject.Inject
import play.api.Environment
import protocols.WorkerProtocol.GetAllEducations

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class EducationManager @Inject()(val environment: Environment,
                                 educationDao: EducationDao)
                                (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout = Timeout(60.seconds)

  def receive = {
    case GetAllEducations =>
      getEducations.pipeTo(sender())

    case _ => log.info(s"received unknown message")
  }

  private def getEducations = {
    educationDao.getEducations
  }

}