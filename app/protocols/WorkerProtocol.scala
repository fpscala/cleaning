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
                      phoneHash: Array[Byte],
                      warnings: Option[JsValue] = None,
                      pensionNumber: Int,
                      ITN: Long,                   // ITN (Individual Taxpayer Number)
                      gender: Int,
                      birthDay: Date,
                      birthPlace: String,
                      education: Int,
                      password: String)

  implicit val workerFormat: OFormat[Worker] = Json.format[Worker]

  case object GetGenderList
  case class Gender(id: Option[Int] = None, name: String, code: String)

  implicit val genderFormat = Json.format[Gender]

  case class CheckLoginAndPassword(customerData: Auth)

  case class Auth(login:String, password: String)

  implicit val authorFormat = Json.format[Auth]

  case object GetAllEducations
  case class Education(id: Option[Int] = None, name: String, code: String)

  implicit val educationFormat = Json.format[Education]


  case class AddImage(originalFileName: String, content: Array[Byte])
  case class GetImage(fileId: String)
  case object GetImages
}
