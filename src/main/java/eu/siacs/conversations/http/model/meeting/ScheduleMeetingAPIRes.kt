package eu.siacs.conversations.http.model.meeting

import com.google.gson.annotations.SerializedName


data class ScheduleMeetingAPIRes(

    @SerializedName("Success") var Success: Boolean? = null,
    @SerializedName("Message") var Message: String? = null,
    @SerializedName("Error") var Error: String? = null,
    @SerializedName("Data") var Data: ArrayList<ScheduleMeetingModel> = arrayListOf(),
    @SerializedName("PageSize") var PageSize: Int? = null,
    @SerializedName("PageNumber") var PageNumber: Int? = null

)