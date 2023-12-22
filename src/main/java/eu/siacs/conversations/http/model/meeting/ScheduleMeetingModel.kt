package eu.siacs.conversations.http.model.meeting

import com.google.gson.annotations.SerializedName


data class ScheduleMeetingModel(

    @SerializedName("scheduleUniqueId") var scheduleUniqueId: String? = null,
    @SerializedName("meetingId") var meetingId: String? = null,
    @SerializedName("meetingName") var meetingName: String? = null,
    @SerializedName("meetingDate") var meetingDate: String? = null,
    @SerializedName("meetingDuration") var meetingDuration: String? = null,
    @SerializedName("meetingStartTime") var meetingStartTime: String? = null,
    @SerializedName("accountId") var accountId: String? = null,
    @SerializedName("isDeleted") var isDeleted: Boolean? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("createdBy") var createdBy: String? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("lastModifiedBy") var lastModifiedBy: String? = null,
    @SerializedName("lastModified") var lastModified: String? = null

) {
    override fun toString(): String {
        return "ScheduleMeetingModel(" +
                "scheduleUniqueId=$scheduleUniqueId, " +
                "meetingId=$meetingId, " +
                "meetingName=$meetingName, " +
                "meetingDate=$meetingDate, " +
                "meetingDuration=$meetingDuration, " +
                "meetingStartTime=$meetingStartTime, " +
                "accountId=$accountId, " +
                "isDeleted=$isDeleted, " +
                "id=$id, " +
                "createdBy=$createdBy, " +
                "created=$created, " +
                "lastModifiedBy=$lastModifiedBy, " +
                "lastModified=$lastModified)"
    }
}