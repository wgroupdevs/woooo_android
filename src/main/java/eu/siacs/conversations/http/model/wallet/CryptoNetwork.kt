package eu.siacs.conversations.http.model.wallet

import com.google.gson.annotations.SerializedName
import eu.siacs.conversations.services.AvatarService.Avatarable
import eu.siacs.conversations.utils.UIHelper


data class CryptoNetwork(

    @SerializedName("name") var name: String? = null,
    @SerializedName("shortName") var shortName: String? = null,
    @SerializedName("networkName") var networkName: String? = null,
    @SerializedName("networkShortName") var networkShortName: String? = null,
    @SerializedName("rpcUrl") var rpcUrl: String? = null,
    @SerializedName("chainID") var chainID: Int? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("blockExplorerUrl") var blockExplorerUrl: String? = null,
    @SerializedName("blockchainNetworkType") var blockchainNetworkType: Int? = null,
    @SerializedName("blockchainType") var blockchainType: Int? = null,
    @SerializedName("bluid") var bluid: String? = null,
    @SerializedName("isDeleted") var isDeleted: Boolean? = null,
    @SerializedName("gasLimit") var gasLimit: Int? = null,
    @SerializedName("gasPrice") var gasPrice: Int? = null,
    @SerializedName("chainHexId") var chainHexId: String? = null,
    @SerializedName("coinIcon") var coinIcon: String? = null

)