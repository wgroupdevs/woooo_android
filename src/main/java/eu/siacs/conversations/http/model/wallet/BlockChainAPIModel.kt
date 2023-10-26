package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class BlockChainAPIModel(

    @SerializedName("pageNumber") var pageNumber: Int? = null,
    @SerializedName("pageSize") var pageSize: Int? = null,
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("errors") var errors: String? = null,
    @SerializedName("data") var data: ArrayList<CryptoNetwork> = arrayListOf()

)