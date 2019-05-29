package protocols

import java.util.Date

import play.api.libs.json.{JsValue, Json}

object WorkerProtocol {
    case class AddWorker(workers: Worker)
    case class Worker(surname: String,
                      firstName: String,
                      lastName: Option[String] = None,
                      address: String,
                      phone: String,
                      passportSeriesAndNumber: String,
                      dayGettingPassport: Date,
                      photoName: String,
                      phoneHash: String,
                      warnings: Option[JsValue] = None,
                      pensionNumber: Int,
                      ITN: Int,                   // ITN (Individual Taxpayer Number)
                      gender: Int,
                      birthDay: Date,
                      birthPlace: String,
                      education: Int)

  implicit val workerFormat = Json.format[Worker]

  case class AddImage(originalFileName: String, content: Array[Byte])
  case class GetImage(fileId: String)
  case object GetImages
}
