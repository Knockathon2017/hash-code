package service

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.json.{JsString, JsSuccess, Format, JsValue}
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by amans on 19/8/17.
  */
class JiraService @Inject()(wsclient: WSClient) {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss z")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  def readJiraTickets() = {

   val q =  wsclient.url("https://testfipple.atlassian.net/rest/api/2/search?jql=assignee=fippletask&fields=id,key,status,created,updated,summary,description")
      .withHeaders("Content-Type" -> "application/json", "Authorization" -> "Basic ZmlwcGxldGFzazphc2RmMTIzNA==")
      .get()

    q.map{
      case res if res.status == 200 =>
      val issueList =   (res.json \ "issues").as[List[JsValue]]
        issueList map {
          x=>
            val key = (x \ "key").asOpt[String]
            val description = (x \ "fields"\"description").asOpt[String]
            val title = (x \ "fields"\"summary").asOpt[String]
            val status = (x \"fields"\ "status" \ "name").asOpt[String]
            val updated = (x \"fields"\ "updated").as[String]
            val created = (x \"fields"\ "created").as[String]
            fromUtcStrToTimestamp(updated)
            fromUtcStrToTimestamp(created)
            println(key,title,description,status,updated,created)
        }
    }
  }

  def fromUtcStrToTimestamp(utcStr: String) = {
    val pattern = "yyyy-MM-dd'T'HH:mm:sss.SSSz"
    try {
      val utcDate = new SimpleDateFormat(pattern).parse(utcStr)
      val timestamp = new Timestamp(utcDate.getTime())
      Some(timestamp)
    } catch {
      case default: Throwable =>
        Logger.error("Error Parsing Date :" + utcStr, default)
        None
    }
  }

}
