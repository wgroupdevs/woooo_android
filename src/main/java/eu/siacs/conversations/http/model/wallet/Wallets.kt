package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName


data class Wallets(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("accountId") var accountId: String? = null,
    @SerializedName("walletUniqueId") var walletUniqueId: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("walletAddress") var walletAddress: String? = null,
    @SerializedName("barcodeImageURL") var barcodeImageURL: String? = null,
    @SerializedName("walletType") var walletType: String? = null,
    @SerializedName("desTag") var desTag: String? = null,
    @SerializedName("publicKey") var publicKey: String? = null,
    @SerializedName("privateKey") var privateKey: String? = null,
    @SerializedName("balance") var balance: Double? = null,
    @SerializedName("balanceStoshis") var balanceStoshis: Int? = null,
    @SerializedName("bonus") var bonus: String? = null,
    @SerializedName("refBonus") var refBonus: String? = null,
    @SerializedName("mnemonics") var mnemonics: String? = null,
    @SerializedName("pendingBalance") var pendingBalance: Double? = null,
    @SerializedName("txIndex") var txIndex: String? = null,
    @SerializedName("underProcessed") var underProcessed: String? = null,
    @SerializedName("balanceInDollar") var balanceInDollar: String? = null,
    @SerializedName("addressIndex") var addressIndex: String? = null,
    @SerializedName("zenAmount") var zenAmount: Double? = null,
    @SerializedName("walletActivationKey") var walletActivationKey: Boolean? = null

)