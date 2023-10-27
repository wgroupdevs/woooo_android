package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName

data class TransactionAPIModel(

    @SerializedName("Success") var Success: Boolean? = null,
    @SerializedName("Message") var Message: String? = null,
    @SerializedName("Error") var Error: String? = null,
    @SerializedName("Data") var transactions: ArrayList<Transaction> = arrayListOf(),
    @SerializedName("PageSize") var PageSize: Int? = null,
    @SerializedName("PageNumber") var PageNumber: Int? = null

)