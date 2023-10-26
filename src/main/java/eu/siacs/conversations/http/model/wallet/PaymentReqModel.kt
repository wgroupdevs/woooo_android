package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class PaymentReqModel(

    @SerializedName("accountId") var accountId: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("transactionHash") var transactionHash: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("paymentType") var paymentType: String? = null

)