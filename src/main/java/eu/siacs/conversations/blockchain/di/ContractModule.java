package eu.siacs.conversations.blockchain.di;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import eu.siacs.conversations.blockchain.NetWorkGasProvider;
import eu.siacs.conversations.blockchain.contracts.AddressMapper;
import eu.siacs.conversations.blockchain.contracts.ContractAddress;
import eu.siacs.conversations.utils.WOONetwork;

@Module
@InstallIn(SingletonComponent.class)
public class ContractModule {

    @Singleton
    @Provides
    Web3j provideWeb3j() {
        return Web3j.build(new HttpService(WOONetwork.WOO_RPC_URL));
    }

    @Singleton
    @Provides
    AddressMapper provideAddressMapper(Web3j web3j) {
        Credentials credentials = Credentials.create("cbe55a41b7aab4e08f6a62599294602ab4b81870c5be6f0307f68d54d390bc13");
        return AddressMapper.load(ContractAddress.ADDRESS_MAPPER,web3j, credentials, new NetWorkGasProvider());
    }


}
