package controllers

import com.google.inject.Inject
import models.User
import play.api.mvc._
import repo.UserRepository
import service.JiraService
import utils.JsonFormat._

import scala.concurrent.ExecutionContext


class UserController @Inject()(userRepository: UserRepository,jiraService: JiraService)(implicit ec: ExecutionContext) extends Controller {


  def insertUser() = Action.async(parse.json){
    implicit  req =>
      val jiraUserName = (req.body \ "jira_user_name").asOpt[String].getOrElse("")
      val dashboardUserName = (req.body \ "dashboard_user_name").asOpt[String].getOrElse("")
      val dashboardPassword = (req.body \ "dashboard_password").asOpt[String].getOrElse("")
      val outlookUserName = (req.body \ "outlook_user_name").asOpt[String].getOrElse("")
      val outlookPassword = (req.body \ "outlook_password").asOpt[String].getOrElse("")

      userRepository.insert(User(None,jiraUserName,dashboardUserName,dashboardPassword,outlookUserName,outlookPassword)) map {
      x=>
        jiraService.readJiraTickets()
        Ok
      }
  }

}



