package eu.siacs.conversations.http.model.meeting

import com.google.gson.annotations.SerializedName

class HistoryMeetingModel(


    @SerializedName("meetingUserUniqueId") var meetingUserUniqueId: String? = null,
    @SerializedName("meetingUniqueId") var meetingUniqueId: String? = null,
    @SerializedName("meetingId") var meetingId: String? = null,
    @SerializedName("meetingName") var meetingName: String? = null,
    @SerializedName("startTime") var startTime: String? = null,
    @SerializedName("endTime") var endTime: String? = null,
    @SerializedName("userId") var userId: String? = null,
    @SerializedName("userRole") var userRole: String? = null,
    @SerializedName("duration") var duration: String? = null

)