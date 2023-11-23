package eu.siacs.conversations.blockchain.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import eu.siacs.conversations.blockchain.contracts.AddressMapper;
import eu.siacs.conversations.blockchain.interfaces.AddressMapperCallback;

@HiltViewModel
public class AddressMapperViewModel extends ViewModel {

    private String TAG = "AddressMapper_TAG";
    private final AddressMapper addressMapper;

    @Inject
    AddressMapperViewModel(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public void checkAddressMapper() throws ExecutionException, InterruptedException {
        Log.d(TAG, "ADDRESS_MAPPER : " + this.addressMapper.getOperatorContract().sendAsync().get());
    }

    public void setAddressPhoneNumber(String userAddress, String phoneNumber, AddressMapperCallback listen) {
        Log.d(TAG, "ADDRESS_MAPPER :setAddressPhoneNumber started....");
        CompletableFuture<TransactionReceipt> sendAsyncFuture = this.addressMapper.setAddressPhoneNumber(userAddress, phoneNumber).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.setAddressPhoneNumber(true);
            Log.d(TAG, "ADDRESS_MAPPER :TransactionHash ..... " + result.getTransactionHash());
        }).exceptionally(throwable -> {
            listen.setAddressPhoneNumber(false);
            System.err.println("Error: " + throwable.getMessage());
            return null;
        });
    }

    public void getAddressByPhoneNumber(String phoneNumber) {
        Log.d(TAG, "ADDRESS_MAPPER :getAddressByPhoneNumber started....");
        CompletableFuture<String> sendAsyncFuture = this.addressMapper.getAddressByPhoneNumber(phoneNumber).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            Log.d(TAG, "ADDRESS_MAPPER :TransactionHash ..... " + result);
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        });


    }


}
