package utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger

/**
  * Created by abhishekj on 19/8/17.
  */
object Helpers {

  def fromUtcStrToTimestamp(utcStr: String) = {
    val pattern = "yyyy-MM-dd'T'HH:mm:sss.SSSz"
    try {
      val utcDate = new SimpleDateFormat(pattern).parse(utcStr)
      val timestamp = new Timestamp(utcDate.getTime())
//      Some(timestamp)
      None
    } catch {
      case default: Throwable =>
        Logger.error("Error Parsing Date :" + utcStr, default)
        None
    }
  }

  def currentDate = {
    val pattern = "yyyy-MM-dd'T'00:00:00.0000000"
    val date = new Date()
    try {
      val utcDate: String = new SimpleDateFormat(pattern).format(date.getTime)
     Some(utcDate)
    } catch {
      case default: Throwable =>
        Logger.error("Error Parsing Date :" + default)
        None
    }
  }

}
