package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class WalletOverviewData(

    @SerializedName("currencies") var currencies: ArrayList<Currencies> = arrayListOf(),
    @SerializedName("wallets") var wallets: ArrayList<Wallets> = arrayListOf(),
    @SerializedName("payments") var payments: ArrayList<Payment> = arrayListOf(),
//    @SerializedName("userActivities") var userActivities: ArrayList<String> = arrayListOf(),
    @SerializedName("currencyRates") var currencyRates: ArrayList<CurrencyRates> = arrayListOf()

)