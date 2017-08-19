package controllers

import com.google.inject.Inject
import models.User
import play.api.mvc._
import repo.UserRepository
import service.{JiraService, OutlookService}
import utils.Helpers
import utils.JsonFormat._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


class UserController @Inject()(userRepository: UserRepository,jiraService: JiraService, outlookService: OutlookService)(implicit ec: ExecutionContext) extends Controller {


  def insertUser() = Action.async(parse.json){
    implicit  req =>
      val jiraUserName = (req.body \ "jira_user_name").asOpt[String].getOrElse("")
      val dashboardUserName = (req.body \ "dashboard_user_name").asOpt[String].getOrElse("")
      val dashboardPassword = (req.body \ "dashboard_password").asOpt[String].getOrElse("")
      val outlookUserName = (req.body \ "outlook_user_name").asOpt[String].getOrElse("")
      val outlookPassword = (req.body \ "outlook_password").asOpt[String].getOrElse("")
      val deviceToken = (req.body \ "device_token").asOpt[String].getOrElse("")
      val startDate = Helpers.currentDate.getOrElse("2017-07-28T06:30:00.0000000")
        val endDate = Helpers.currentDate.getOrElse("2017-07-28T06:30:00.0000000")
      userRepository.insert(User(None,jiraUserName,dashboardUserName,dashboardPassword,outlookUserName,outlookPassword,deviceToken)) map {
      x=>
        jiraService.readJiraTickets(deviceToken)
        outlookService.readOutlookEvents(outlookUserName, outlookPassword, startDate, endDate)
        Ok
      }
  }

}



