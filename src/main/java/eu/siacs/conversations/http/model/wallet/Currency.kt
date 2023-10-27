package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Currency(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("hexId") var hexId: String? = null,
    @SerializedName("imgURL") var imgURL: String? = null,
    @SerializedName("rateYEN") var rateYEN: Double? = null,
    @SerializedName("rateUSD") var rateUSD: Double? = null,
    @SerializedName("rateEUR") var rateEUR: Double? = null

)