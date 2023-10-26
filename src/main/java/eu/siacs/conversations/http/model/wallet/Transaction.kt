package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName

data class Transaction(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("paymentDate") var date: String? = null,
    @SerializedName("paymentType") var type: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("confirms") var confirms: Int? = null

)