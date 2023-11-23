package eu.siacs.conversations.blockchain.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import eu.siacs.conversations.blockchain.contracts.Factory;
import eu.siacs.conversations.blockchain.interfaces.FactoryContractCallback;

@HiltViewModel
public class FactoryContractViewModel extends ViewModel {
    private String TAG = "FactoryContract_TAG";
    private final Factory factory;


    @Inject
    public FactoryContractViewModel(Factory factory) {
        this.factory = factory;
    }

    public void createChatContract(String phoneNumber1, String phoneNumber2, FactoryContractCallback listen) {
        Log.d(TAG, "createChatContract started....");
        CompletableFuture<TransactionReceipt> sendAsyncFuture = this.factory.CreateChatContract(phoneNumber1, phoneNumber2).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onChatContractCreated(result.getTransactionHash());
            Log.d(TAG, "createChatContract RESULT .... " + result );

        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        });

    }


}
