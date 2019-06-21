package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.GenderDao
import dao.CountDao
import javax.inject.Inject
import play.api.Environment
import protocols.OrderProtocol.GetCountList
import protocols.WorkerProtocol.GetGenderList

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class GenderManager @Inject()(val environment: Environment,
                              genderDao: GenderDao,
                              countDao: CountDao)
                             (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive: PartialFunction[Any, Unit] = {
    case GetGenderList =>
      getGender.pipeTo(sender())

    case GetCountList =>
      getCount.pipeTo(sender())

    case _ => log.info(s"received unknown message")
  }

  private def getGender = {
    genderDao.getGenders()
  }
  private def getCount = {
    countDao.getCounts()
  }
}