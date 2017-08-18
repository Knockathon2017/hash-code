package models

import java.sql.Timestamp

case class User(
               id:Option[Long],
               jiraUserName: String,
               dashboardUserName:String,
               dashboardPassword:String,
               outlookUserName:String,
               outlookPassword:String,
               deviceToken:String
               )


case class TimeSheet(
                      id:Option[Long],
                      project: String,
                      description:String,
                      startTime:Timestamp,
                      endTime:Timestamp
               )