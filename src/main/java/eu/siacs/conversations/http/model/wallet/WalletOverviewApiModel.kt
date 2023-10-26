package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class WalletOverviewApiModel(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("error") var error: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: WalletOverviewData? = WalletOverviewData()

)