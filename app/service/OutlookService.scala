package service

import java.security.Timestamp

import com.google.inject.Inject
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSClient, WSRequest}
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by abhishekj on 19/8/17.
  */
class OutlookService @Inject()(wsclient: WSClient,configuration: Configuration) {

  val office_key = configuration.getString("office.key").getOrElse("")
  def readOutlookEvents(username: String, password: String, startTime: String, endTime: String) = {
    Logger.info("startTime:- "+ startTime)
    Logger.info("endTime:- "+endTime)


  }


}
