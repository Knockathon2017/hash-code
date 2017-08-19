package service

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.google.inject.Inject
import models.TimeSheet
import play.api.{Configuration, Logger}
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest}
import repo.TimeSheetRepository
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by amans on 19/8/17.
  */
class JiraService @Inject()(wsclient: WSClient, configuration: Configuration, timeSheetRepository: TimeSheetRepository) {

  val gcm_key = configuration.getString("google.key").getOrElse("")

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss z")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }

    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  def readJiraTickets(dusername: String, dpassword: String, deviceToken: String, userId: Long, startTime: String, endTime: String,accessToken:String) = {

    val currentDate = Helpers.currentDate

    val jira = wsclient.url("https://testfipple.atlassian.net/rest/api/2/search?jql=assignee=fippletask&fields=id,key,status,created,updated,summary,description")
      .withHeaders("Content-Type" -> "application/json", "Authorization" -> "Basic ZmlwcGxldGFzazphc2RmMTIzNA==")





    val cal = wsclient.url(s"https://graph.microsoft.com/v1.0/me/calendarView?startDateTime=$startTime&endDateTime=$endTime" + "&$select=Subject,Start,End")
      .withHeaders("Authorization" -> s"Bearer $accessToken", "Content-Type" -> "application/json", "Prefer" -> "outlook.timezone")


    /*val q = wsclient.url("https://graph.microsoft.com/v1.0/me/calendarView?startDateTime=2017-07-28T06:30:00.0000000&endDateTime=2017-07-28T06:30:00.0000000&$select=Subject,Start,End")
       .withHeaders("Authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFBOWtUa2xoVnk3U0pUR0F6Ui1wMUJjZW9ZYUFMcTh3ekR4YWVGc1Z3Szcyc2VEUUVfXzNMZ3VrOWxjZ0dwU0xLT0VCV3A5bUoyYmU2M2tYbUd4Z1BMREtQemJScExnS0ZTRmlVVmhtWXZ1ZGlBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIiwia2lkIjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20vIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDY2MDY0YTctZjg0ZS00MTY1LWE5YzktYjA0YjFhYTkwNGViLyIsImlhdCI6MTUwMzEwNDg0MywibmJmIjoxNTAzMTA0ODQzLCJleHAiOjE1MDMxMDg3NDMsImFjciI6IjEiLCJhaW8iOiJBU1FBMi84RUFBQUFmVS82OWhsK29YRklGTFZ1LzJlNHovcm5xN3F0VmkrSXRTWjdsQUx5enlFPSIsImFtciI6WyJwd2QiXSwiYXBwX2Rpc3BsYXluYW1lIjoiRmlwcGxlIiwiYXBwaWQiOiI2ZDJhMjY5Ni0xZDc1LTRkYzItODgxMC00MWY1ZDljNjQ0ODkiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IkphaXN3YWwiLCJnaXZlbl9uYW1lIjoiQWJoaXNoZWsiLCJpcGFkZHIiOiIxMTkuODIuNzguMjEwIiwibmFtZSI6IkFiaGlzaGVrIEphaXN3YWwiLCJvaWQiOiJlY2RmYmE3MC1lYWE1LTQ3YWQtYjE4MC1kMGZmODYzNzg1ZTYiLCJwbGF0ZiI6IjE0IiwicHVpZCI6IjEwMDMzRkZGOUYyOTY3QjYiLCJzY3AiOiJDYWxlbmRhcnMuUmVhZCBDYWxlbmRhcnMuUmVhZC5TaGFyZWQgQ2FsZW5kYXJzLlJlYWRXcml0ZSBDYWxlbmRhcnMuUmVhZFdyaXRlLlNoYXJlZCBDb250YWN0cy5SZWFkIENvbnRhY3RzLlJlYWQuU2hhcmVkIENvbnRhY3RzLlJlYWRXcml0ZSBDb250YWN0cy5SZWFkV3JpdGUuU2hhcmVkIFVzZXIuUmVhZCIsInN1YiI6InQ3cFlhZGFfRDFhc1NYYnRib2dhQ1ROSU9VQjlLYUNJUkxEdC1zbDNoVVUiLCJ0aWQiOiJkNjYwNjRhNy1mODRlLTQxNjUtYTljOS1iMDRiMWFhOTA0ZWIiLCJ1bmlxdWVfbmFtZSI6ImFiaGlzaGVrakBleHplby5jb20iLCJ1cG4iOiJhYmhpc2hla2pAZXh6ZW8uY29tIiwidXRpIjoiY2NZLUowUkcyRWlHZWZwSXNGSXBBQSIsInZlciI6IjEuMCJ9.Q_D3LCamudWjrQ3r3z5zKo-po9PSJzzPbuXA26g6kQGL05LjXJHBXrN2HC0SNEhX7wDr8gJUeK67cew_ZWbDlX73Aygrlz6w1eLPjg78VuYh3RbpwSQvu9gCibmiZJKq0GxMWGyqFL0thhCpB2aYK3RkDPKqWJl0odqvNZGcOA2CDtEctfQAdf7nS8R1WNDhH3uTQzl4ImO5IHtEHQs4U_ivV83QdFlfGsgpzXBTOdCrIRTf9x4QRfkh3wL_IidWsR9Voi2dMyFsXQ0XaNkHVsFreXcajuUPPou1W1O31KlkHsMSPZRSOg-oDlD4p7ZDVxxHwYRIy1fD2QQqS_UmfA", "Content-Type" -> "application/json")
       .get()*/

    val timeSheetList = scala.collection.mutable.ListBuffer[TimeSheet]()

    for {
      j <- jira.get()
      c <- cal.get()

    } yield {

      if (j.status == 200) {
        val issueList = (j.json \ "issues").as[List[JsValue]]
        issueList map {
          x =>
            val key = (x \ "key").asOpt[String].getOrElse("")
            val description = (x \ "fields" \ "description").asOpt[String].getOrElse("")
            val updated = (x \ "fields" \ "updated").as[String]
            val created = (x \ "fields" \ "created").as[String]
            timeSheetList += TimeSheet(None, userId, "Exzeo", "Resolved " + key + " | " + description, key, Helpers.fromUtcStrToTimestamp(updated).get, Helpers.fromUtcStrToTimestamp(created).get, Helpers.fromUtcStrToTimestamp(updated).get)

        }

        if (c.status == 200) {
          val calList = (c.json \ "value").as[List[JsValue]]
          calList map {
            x =>
              val description = (x \ "subject").asOpt[String].getOrElse("")
              timeSheetList += TimeSheet(None, userId, "General", "Attended meeting | " + description, "General", Helpers.fromUtcStrToTimestampForCal(endTime).get, Helpers.fromUtcStrToTimestampForCal(startTime).get, Helpers.fromUtcStrToTimestampForCal(endTime).get)

          }
        }
      }

      timeSheetRepository.insert(timeSheetList.toList)


      timeSheetList.toList.take(4)

    }

  }

  def sendNoti(token:String,userId:Long) = {
          val urlRequest: WSRequest =
            wsclient.url("https://fcm.googleapis.com/fcm/send")
              .withHeaders(
                "Content-Type" -> "application/json",
                "Authorization" -> s"key=$gcm_key")


          def payload(to: String) = Json.obj(
            "to" -> to,
            "notification" -> Json.obj(
              "title" -> "# Code",
              "body" -> s"TimeSheet Filled",
              "user_id" -> userId
            )
          )

          urlRequest.post(payload(token)) map {
            y => Logger.info(s"Android Notification ${y.body}")
          }
        }
  }

