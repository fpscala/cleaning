package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.AuthorizeDao
import javax.inject.Inject
import play.api.Environment
import protocols.WorkerProtocol.GetAllLoginAndPassword

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class AuthorizeManager @Inject()(val environment: Environment,
                                 authorizeDao: AuthorizeDao
                                )
                                (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case GetAllLoginAndPassword =>
      getAllLoginAndPassword.pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def getAllLoginAndPassword ={
    authorizeDao.getAuthor()
  }

}
