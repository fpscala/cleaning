package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.AuthorizeDao
import javax.inject.Inject
import play.api.Environment
import protocols.WorkerProtocol.{Auth, CheckLoginAndPassword}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

class AuthorizeManager @Inject()(val environment: Environment,
                                 authorizeDao: AuthorizeDao
                                )
                                (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case CheckLoginAndPassword(customerData) =>
      checkLoginAndPassword(customerData).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def checkLoginAndPassword(customerData:Auth): Future[Option[Auth]] = {
    authorizeDao.getAuthor(customerData).map { data =>
      data
    }
  }

}
