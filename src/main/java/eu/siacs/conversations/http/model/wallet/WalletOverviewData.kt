package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class WalletOverviewData(

    @SerializedName("currency") var currency: ArrayList<Currency> = arrayListOf(),
    @SerializedName("wallet") var wallet: ArrayList<Wallet> = arrayListOf(),
    @SerializedName("walletAddress") var walletAddress: String? = null

)