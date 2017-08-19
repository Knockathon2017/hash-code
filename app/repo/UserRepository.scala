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
    val deviceToken: Rep[String] = column[String]("device_token")
    val accessToken: Rep[String] = column[String]("access_token")

    def * = (id.?, jiraUserName, dashboardUserName, dashboardPassword, outlookUserName, outlookPassword, deviceToken, accessToken) <>(User.tupled, User.unapply)
  }

  val userQuery = TableQuery[UserTable]


}


class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends UserTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val autoInc = userQuery returning userQuery.map(_.id)

  def insert(user: User) = {
    db.run(autoInc += user)
  }

  def update(email: String, accessToken: String) = {
    val q = for {
      u <- userQuery.filter(_.outlookUserName === email)
    } yield u.accessToken

    db.run(q.update(accessToken))
  }

  def getUser(email: String) = db.run(userQuery.filter(_.outlookUserName === email).result.headOption)

  def getLatestUser = db.run(userQuery.sortBy(_.id).sorted(_.id.desc).result.headOption)

}


