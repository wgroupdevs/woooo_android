package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Payment(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("paymentUniqueId") var paymentUniqueId: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("paymentDate") var paymentDate: String? = null,
    @SerializedName("toWalletAddress") var toWalletAddress: String? = null,
    @SerializedName("paymentType") var paymentType: String? = null,
    @SerializedName("transectionId") var transectionId: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("amountSent") var amountSent: Double? = null,
    @SerializedName("networkFee") var networkFee: Double? = null,
    @SerializedName("fee") var fee: Double? = null,
    @SerializedName("confirms") var confirms: Int? = null,
    @SerializedName("accountId") var accountId: String? = null,
    @SerializedName("paymentStatus_Id") var paymentStatusId: Int? = null,
    @SerializedName("blockchain") var blockchain: String? = null,
    @SerializedName("transactionHash") var transactionHash: String? = null

)