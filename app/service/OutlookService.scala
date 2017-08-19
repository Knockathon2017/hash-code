package service

import java.security.Timestamp

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSClient, WSRequest}
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by abhishekj on 19/8/17.
  */
class OutlookService @Inject()(wsclient: WSClient) {

  def readOutlookEvents(username: String, password: String, startTime: String, endTime: String) = {
    Logger.info("startTime:- "+ startTime)
    Logger.info("endTime:- "+endTime)
    val q = wsclient.url("https://graph.microsoft.com/v1.0/me/calendarView?startDateTime=2017-07-28T06:30:00.0000000&endTime=2017-07-28T06:30:00.0000000&$select=Subject,Start,End")
      .withHeaders("Authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFBOWtUa2xoVnk3U0pUR0F6Ui1wMUJjVmNJTU9sRjdkV0xWQlZWRHZDRG5hSTdwM2FZOGc0OHliZXd2YUxRZWhXdmlFVmFqYW9YdjF4MVpzSVlMSm1ITEVmVnpXVUQwSkJaclZsekN0dGFJU0NBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIiwia2lkIjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20vIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDY2MDY0YTctZjg0ZS00MTY1LWE5YzktYjA0YjFhYTkwNGViLyIsImlhdCI6MTUwMzEwOTAzNSwibmJmIjoxNTAzMTA5MDM1LCJleHAiOjE1MDMxMTI5MzUsImFjciI6IjEiLCJhaW8iOiJZMkZnWVBoZEwyay8rOHpyYjJuNjBac1g2M1ZtNkxnbnAyMVh0WGgxU1diM1FxMFFUemtBIiwiYW1yIjpbInB3ZCJdLCJhcHBfZGlzcGxheW5hbWUiOiJGaXBwbGUiLCJhcHBpZCI6IjZkMmEyNjk2LTFkNzUtNGRjMi04ODEwLTQxZjVkOWM2NDQ4OSIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSmFpc3dhbCIsImdpdmVuX25hbWUiOiJBYmhpc2hlayIsImlwYWRkciI6IjExOS44Mi43OC4yMTAiLCJuYW1lIjoiQWJoaXNoZWsgSmFpc3dhbCIsIm9pZCI6ImVjZGZiYTcwLWVhYTUtNDdhZC1iMTgwLWQwZmY4NjM3ODVlNiIsInBsYXRmIjoiMTQiLCJwdWlkIjoiMTAwMzNGRkY5RjI5NjdCNiIsInNjcCI6IkNhbGVuZGFycy5SZWFkIENhbGVuZGFycy5SZWFkLlNoYXJlZCBDYWxlbmRhcnMuUmVhZFdyaXRlIENhbGVuZGFycy5SZWFkV3JpdGUuU2hhcmVkIENvbnRhY3RzLlJlYWQgQ29udGFjdHMuUmVhZC5TaGFyZWQgQ29udGFjdHMuUmVhZFdyaXRlIENvbnRhY3RzLlJlYWRXcml0ZS5TaGFyZWQgVXNlci5SZWFkIiwic3ViIjoidDdwWWFkYV9EMWFzU1hidGJvZ2FDVE5JT1VCOUthQ0lSTER0LXNsM2hVVSIsInRpZCI6ImQ2NjA2NGE3LWY4NGUtNDE2NS1hOWM5LWIwNGIxYWE5MDRlYiIsInVuaXF1ZV9uYW1lIjoiYWJoaXNoZWtqQGV4emVvLmNvbSIsInVwbiI6ImFiaGlzaGVrakBleHplby5jb20iLCJ1dGkiOiJHTFhTeHB2RENVbUREYm9hb29vc0FBIiwidmVyIjoiMS4wIn0.rYRiMgLIrM06FDwb59JAS8Yd58ZvtcMS9kMLa24R3Esd3wejds5DpCBxqKg9qKnadPimGPq1Yeun-FMBnKAthD4xveCwWEnhD7BV34ZaOaYgqcivffGVKxffEq6TPlwZSP78KNLGkodQ-Sqt6oA1VoWiRQolTndwz0YA5XrJnOJGqrXDW6TnLD6Xk61OgNNr9THaN-DJgqFQKy78yQAQX9gVPt-g2b7SFBvf30V5OBaRVKzKfvx0hAagGWgDST9FkcrSFriWL8tQCEvGbhKCMrTRw7ah1qBVceX7rE7LZgf7Em0E1xMMKfZ96B2srIP1prwXdAKzYa5NFzijncKMxg", "Content-Type" -> "application/json")
      .get()
   /*val q = wsclient.url("https://graph.microsoft.com/v1.0/me/calendarView?startDateTime=2017-07-28T06:30:00.0000000&endDateTime=2017-07-28T06:30:00.0000000&$select=Subject,Start,End")
      .withHeaders("Authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFBOWtUa2xoVnk3U0pUR0F6Ui1wMUJjZW9ZYUFMcTh3ekR4YWVGc1Z3Szcyc2VEUUVfXzNMZ3VrOWxjZ0dwU0xLT0VCV3A5bUoyYmU2M2tYbUd4Z1BMREtQemJScExnS0ZTRmlVVmhtWXZ1ZGlBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIiwia2lkIjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20vIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDY2MDY0YTctZjg0ZS00MTY1LWE5YzktYjA0YjFhYTkwNGViLyIsImlhdCI6MTUwMzEwNDg0MywibmJmIjoxNTAzMTA0ODQzLCJleHAiOjE1MDMxMDg3NDMsImFjciI6IjEiLCJhaW8iOiJBU1FBMi84RUFBQUFmVS82OWhsK29YRklGTFZ1LzJlNHovcm5xN3F0VmkrSXRTWjdsQUx5enlFPSIsImFtciI6WyJwd2QiXSwiYXBwX2Rpc3BsYXluYW1lIjoiRmlwcGxlIiwiYXBwaWQiOiI2ZDJhMjY5Ni0xZDc1LTRkYzItODgxMC00MWY1ZDljNjQ0ODkiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IkphaXN3YWwiLCJnaXZlbl9uYW1lIjoiQWJoaXNoZWsiLCJpcGFkZHIiOiIxMTkuODIuNzguMjEwIiwibmFtZSI6IkFiaGlzaGVrIEphaXN3YWwiLCJvaWQiOiJlY2RmYmE3MC1lYWE1LTQ3YWQtYjE4MC1kMGZmODYzNzg1ZTYiLCJwbGF0ZiI6IjE0IiwicHVpZCI6IjEwMDMzRkZGOUYyOTY3QjYiLCJzY3AiOiJDYWxlbmRhcnMuUmVhZCBDYWxlbmRhcnMuUmVhZC5TaGFyZWQgQ2FsZW5kYXJzLlJlYWRXcml0ZSBDYWxlbmRhcnMuUmVhZFdyaXRlLlNoYXJlZCBDb250YWN0cy5SZWFkIENvbnRhY3RzLlJlYWQuU2hhcmVkIENvbnRhY3RzLlJlYWRXcml0ZSBDb250YWN0cy5SZWFkV3JpdGUuU2hhcmVkIFVzZXIuUmVhZCIsInN1YiI6InQ3cFlhZGFfRDFhc1NYYnRib2dhQ1ROSU9VQjlLYUNJUkxEdC1zbDNoVVUiLCJ0aWQiOiJkNjYwNjRhNy1mODRlLTQxNjUtYTljOS1iMDRiMWFhOTA0ZWIiLCJ1bmlxdWVfbmFtZSI6ImFiaGlzaGVrakBleHplby5jb20iLCJ1cG4iOiJhYmhpc2hla2pAZXh6ZW8uY29tIiwidXRpIjoiY2NZLUowUkcyRWlHZWZwSXNGSXBBQSIsInZlciI6IjEuMCJ9.Q_D3LCamudWjrQ3r3z5zKo-po9PSJzzPbuXA26g6kQGL05LjXJHBXrN2HC0SNEhX7wDr8gJUeK67cew_ZWbDlX73Aygrlz6w1eLPjg78VuYh3RbpwSQvu9gCibmiZJKq0GxMWGyqFL0thhCpB2aYK3RkDPKqWJl0odqvNZGcOA2CDtEctfQAdf7nS8R1WNDhH3uTQzl4ImO5IHtEHQs4U_ivV83QdFlfGsgpzXBTOdCrIRTf9x4QRfkh3wL_IidWsR9Voi2dMyFsXQ0XaNkHVsFreXcajuUPPou1W1O31KlkHsMSPZRSOg-oDlD4p7ZDVxxHwYRIy1fD2QQqS_UmfA", "Content-Type" -> "application/json")
      .get()*/

    q.map {
      case res if res.status == 200 =>
        val issueList = (res.json \ "value").as[List[JsValue]]
        issueList map {
          x =>
            val description = (x \ "subject").asOpt[String]
            val startTime = (x \ "start" \ "dateTime").asOpt[String]
            val endTime = (x \ "end" \ "dateTime").asOpt[String]
            println(description, startTime, endTime)
        }


    }

  }


}
