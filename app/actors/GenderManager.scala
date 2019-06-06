package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.GenderDao
import javax.inject.Inject
import play.api.Environment
import protocols.WorkerProtocol.GetGender

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class GenderManager @Inject()(val environment: Environment,
                              genderDao: GenderDao)
                             (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout = Timeout(60.seconds)

  def receive = {
    case GetGender =>
      getGenders.pipeTo(sender())

    case _ => log.info(s"received unknown message")
  }

  private def getGenders = {
    genderDao.getGenders
  }
}