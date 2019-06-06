package dao

import play.api.db.slick.HasDatabaseConfigProvider
import protocols.OrderProtocol._
import slick.jdbc.JdbcProfile


trait CountComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import utils.PostgresDriver.api._

  class CountTable(tag: Tag) extends Table[Count](tag, "Counts") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Count.tupled, Count.unapply _)
  }
}
