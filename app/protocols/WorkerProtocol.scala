package protocols

import java.util.Date

import play.api.libs.json.{JsValue, Json, OFormat}

object WorkerProtocol {

    case class AddWorker(workers: Worker)
    case class Worker(id: Option[Int] = None,
                      surname: String,
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

  implicit val workerFormat: OFormat[Worker] = Json.format[Worker]

  case object GetGender
  case class Gender(id: Option[Int] = None, name: String, code: String)

  implicit val genderFormat = Json.format[Gender]

  case object GetAllEducations
  case class Education(id: Option[Int] = None, name: String, code: String)

  implicit val educationFormat = Json.format[Education]


  case class AddImage(originalFileName: String, content: Array[Byte])
  case class GetImage(fileId: String)
  case object GetImages
}
