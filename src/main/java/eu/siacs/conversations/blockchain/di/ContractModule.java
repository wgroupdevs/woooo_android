package eu.siacs.conversations.blockchain.di;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import eu.siacs.conversations.BuildConfig;
import eu.siacs.conversations.blockchain.NetWorkGasProvider;
import eu.siacs.conversations.blockchain.contracts.AddressMapper;
import eu.siacs.conversations.blockchain.contracts.ChatContract;
import eu.siacs.conversations.blockchain.contracts.ContractConst;
import eu.siacs.conversations.blockchain.contracts.Factory;
import eu.siacs.conversations.blockchain.contracts.Mapper;
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
    Credentials provideCredentials() {
        return Credentials.create(BuildConfig.CONTRACT_OPERATOR);
    }

    @Singleton
    @Provides
    NetWorkGasProvider provideNetWorkGas() {
        return new NetWorkGasProvider();
    }

    @Singleton
    @Provides
    AddressMapper provideAddressMapper(Web3j web3j, Credentials credentials, NetWorkGasProvider netWorkGasProvider) {
        return AddressMapper.load(ContractConst.ADDRESS_MAPPER, web3j, credentials, netWorkGasProvider);
    }

    @Singleton
    @Provides
    Factory provideFactory(Web3j web3j, Credentials credentials, NetWorkGasProvider netWorkGasProvider) {
        return Factory.load(ContractConst.ADDRESS_MAPPER, web3j, credentials, netWorkGasProvider);
    }

    @Singleton
    @Provides
    Mapper provideMapper(Web3j web3j, Credentials credentials, NetWorkGasProvider netWorkGasProvider) {
        return Mapper.load(ContractConst.ADDRESS_MAPPER, web3j, credentials, netWorkGasProvider);
    }

    @Singleton
    @Provides
    ChatContract ChatContract(Web3j web3j, Credentials credentials, NetWorkGasProvider netWorkGasProvider) {
        return ChatContract.load(ContractConst.ADDRESS_MAPPER, web3j, credentials, netWorkGasProvider);
    }


}
