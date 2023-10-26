package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Rates(

    @SerializedName("rateIn") var rateIn: String? = null,
    @SerializedName("rate") var rate: Double? = null

)