package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class WalletOverviewApiModel(

    @SerializedName("Success") var success: Boolean? = null,
    @SerializedName("Error") var error: String? = null,
    @SerializedName("Message") var message: String? = null,
    @SerializedName("Data") var data: WalletOverviewData? = WalletOverviewData()

)