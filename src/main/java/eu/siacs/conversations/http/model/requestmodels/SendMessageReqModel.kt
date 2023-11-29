package eu.siacs.conversations.http.model.requestmodels

import com.google.gson.annotations.SerializedName


data class SendMessageReqModel (

  @SerializedName("sender"   ) var sender   : String?  = null,
  @SerializedName("receiver" ) var receiver : String?  = null,
  @SerializedName("text"     ) var text     : String?  = null,
  @SerializedName("isMedia"  ) var isMedia  : Boolean? = null

)