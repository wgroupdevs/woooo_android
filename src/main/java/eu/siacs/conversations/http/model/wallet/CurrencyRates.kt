package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class CurrencyRates(

    @SerializedName("rateFor") var rateFor: String? = null,
    @SerializedName("rates") var rates: ArrayList<Rates> = arrayListOf()

)