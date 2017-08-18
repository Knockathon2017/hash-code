package repo

import com.google.inject.Inject

import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


trait UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val jiraUserName: Rep[String] = column[String]("jiraUserName")
    val dashboardUserName: Rep[String] = column[String]("dashboardUserName")
    val dashboardPassword: Rep[String] = column[String]("dashboardPassword")
    val outlookUserName: Rep[String] = column[String]("outlookUserName")
    val outlookPassword: Rep[String] = column[String]("outlookPassword")

    def * = (id.?, jiraUserName, dashboardUserName, dashboardPassword, outlookUserName, outlookPassword) <>(User.tupled, User.unapply)
  }

  val userQuery = TableQuery[UserTable]

}


class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends UserTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(user: User) = {
    db.run(userQuery += user)
  }


}


