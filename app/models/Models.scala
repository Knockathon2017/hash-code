package models

import java.sql.Timestamp

case class User(
                 id: Option[Long],
                 jiraUserName: String,
                 dashboardUserName: String,
                 dashboardPassword: String,
                 outlookUserName: String,
                 outlookPassword: String,
                 deviceToken: String,
                 accessToken:String
               )


case class TimeSheet(
                      id: Option[Long],
                      userId:Long,
                      project: String,
                      description: String,
                      key: String,
                      fillDate: Timestamp,
                      startTime: Timestamp,
                      endTime: Timestamp
                    )