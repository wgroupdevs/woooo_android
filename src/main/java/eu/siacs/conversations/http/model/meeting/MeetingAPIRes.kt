package eu.siacs.conversations.http.model.meeting

import com.google.gson.annotations.SerializedName


data class MeetingAPIRes<T>(
    @SerializedName("Success") var Success: Boolean? = null,
    @SerializedName("Message") var Message: String? = null,
    @SerializedName("Error") var Error: String? = null,
    @SerializedName("Data") var Data: T? = null,
    @SerializedName("PageSize") var PageSize: Int? = null,
    @SerializedName("PageNumber") var PageNumber: Int? = null
)
