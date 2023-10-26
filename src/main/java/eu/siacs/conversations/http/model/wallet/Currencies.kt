package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Currencies(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("threeDigitName") var threeDigitName: String? = null,
    @SerializedName("isFiat") var isFiat: Boolean? = null,
    @SerializedName("rateBTC") var rateBTC: Double? = null,
    @SerializedName("rateUSD") var rateUSD: Double? = null,
    @SerializedName("rateEUR") var rateEUR: Double? = null,
    @SerializedName("rateGBP") var rateGBP: Double? = null,
    @SerializedName("lastUpdate") var lastUpdate: String? = null,
    @SerializedName("txFree") var txFree: String? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("confirms") var confirms: Int? = null,
    @SerializedName("canConvert") var canConvert: Boolean? = null,
    @SerializedName("imgURL") var imgURL: String? = null,
    @SerializedName("balance") var balance: Double? = null,
    @SerializedName("profit") var profit: Double? = null,
    @SerializedName("fee") var fee: Double? = null,
    @SerializedName("masterKey") var masterKey: String? = null,
    @SerializedName("smartContract") var smartContract: String? = null,
    @SerializedName("isToken") var isToken: Boolean? = null,
    @SerializedName("colorCode") var colorCode: String? = null,
    @SerializedName("decimal") var decimal: String? = null,
    @SerializedName("rateYEN") var rateYEN: Double? = null,
    @SerializedName("blockchainId") var blockchainId: String? = null,
    @SerializedName("chainHexId") var chainHexId: String? = null


)