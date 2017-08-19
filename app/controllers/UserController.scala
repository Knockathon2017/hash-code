package controllers

import com.google.inject.Inject
import models.User
import play.api.libs.json.Json
import play.api.mvc._
import repo.{TimeSheetRepository, UserRepository}
import service.{JiraService, OutlookService}
import utils.Helpers
import utils.JsonFormat._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


class UserController @Inject()(userRepository: UserRepository,
                               jiraService: JiraService,
                               outlookService: OutlookService,
                               timeSheetRepository: TimeSheetRepository)(implicit ec: ExecutionContext) extends Controller {


  def insertUser() = Action.async(parse.json) {
    implicit req =>
      val jiraUserName = (req.body \ "jira_user_name").asOpt[String].getOrElse("")
      val dashboardUserName = (req.body \ "dashboard_user_name").asOpt[String].getOrElse("")
      val dashboardPassword = (req.body \ "dashboard_password").asOpt[String].getOrElse("")
      val outlookUserName = (req.body \ "outlook_user_name").asOpt[String].getOrElse("")
      val outlookPassword = (req.body \ "outlook_password").asOpt[String].getOrElse("")
      val deviceToken = (req.body \ "device_token").asOpt[String].getOrElse("")
      val accessToken = (req.body \ "access_token").asOpt[String].getOrElse("")
      userRepository.insert(User(None, jiraUserName, dashboardUserName, dashboardPassword, outlookUserName, outlookPassword, deviceToken, accessToken)) map {
        x =>

          Ok(Json.toJson(Json.obj("user_id" -> x)))
      }
  }

  def getTimeSheet(userId: Long) = Action.async {
    implicit req =>
      timeSheetRepository.getTimeSheets(userId) map {
        x =>
          val startDate = Helpers.startDate.getOrElse("2017-08-19T10:00:00.0000000")
          val endDate = Helpers.endDate.getOrElse("2017-08-19T19:00:00.0000000")
          var i: Int = 8
          var j: Int = i + 2
          Ok(Json.toJson(Json.obj("created_date" -> Helpers.currentDate, "result" ->
            x.take(4).map {
              t =>
                i = j
                j = i + 2
                Json.obj("project_name" -> t.project, "description" -> t.description, "start_time" -> (i.toString + ":00").toString, "end_time" -> (j.toString + ":00").toString)
            }
          )))
      }

  }

  def getUser(email: String) =
    Action.async { implicit req =>
      userRepository.getUser(email) map {
        case Some(x) =>
          Ok(Json.toJson(Json.obj("user_id" -> x.id,
            "dashboardUserName" -> x.dashboardUserName,
            "dashboardPassword" -> x.dashboardPassword,
            "outlookUserName" -> x.outlookUserName,
            "outlookPassword" -> x.outlookPassword)))
        case _ => NotFound

      }
    }

  def updateUser(email: String) = Action.async(parse.json) {
    implicit req =>
      val accessToken = (req.body \ "access_token").asOpt[String].getOrElse("")
      userRepository.update(email, accessToken) flatMap {
        xl =>
          userRepository.getUser(email) flatMap {
            case Some(u) =>
              val startDate = Helpers.startDate.getOrElse("2017-08-19T10:00:00.0000000")
              val endDate = Helpers.endDate.getOrElse("2017-08-19T19:00:00.0000000")
              var i: Int = 8
              var j: Int = i + 2
              jiraService.readJiraTickets(u.dashboardUserName, u.dashboardPassword, u.deviceToken, xl, startDate, endDate, accessToken) map {
                x =>
                  Ok(Json.toJson(Json.obj("user_name" -> u.dashboardUserName, "password" -> u.dashboardPassword, "created_date" -> Helpers.currentDate, "results" ->
                    x.take(4).map { t =>
                      i = j
                      j = i + 2
                      Json.obj("project_name" -> t.project, "description" -> t.description, "start_time" -> (i.toString + ":00").toString, "end_time" -> (j.toString + ":00").toString)
                    }
                  )))
              }
          }
      }
  }

  def pushNotify(email: String) = Action.async {
    implicit req =>

      userRepository.getUser(email) map {
        case Some(x) =>

          jiraService.sendNoti(x.deviceToken, x.id.get)
          Ok
      }
  }

  def getLatestUser = Action.async{
    implicit req =>
      userRepository.getLatestUser map {
        case Some(x) =>
          Ok(Json.toJson(Json.obj("user_id" -> x.id,
            "dashboardUserName" -> x.dashboardUserName,
            "dashboardPassword" -> x.dashboardPassword,
            "outlookUserName" -> x.outlookUserName,
            "outlookPassword" -> x.outlookPassword)))
        case _ => NotFound

      }
  }

}



