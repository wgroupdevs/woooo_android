package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Wallet(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("colorCode") var colorCode: String? = null,
    @SerializedName("balance") var balance: Double? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("walletType") var walletType: String? = null

)