package eu.siacs.conversations.blockchain.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import eu.siacs.conversations.blockchain.contracts.Mapper;
import eu.siacs.conversations.blockchain.interfaces.MapperCallback;

public class MapperViewModel extends ViewModel {

    private final Mapper mapper;
    private String TAG = "MapperViewModel_TAG";

    public MapperViewModel(Mapper mapper) {
        this.mapper = mapper;
    }

    public void addChatContract(String contractAddress, String user1, String user2, MapperCallback listen) {
        Log.d(TAG, "MAIN_MAPPER :addChatContract started....");
        CompletableFuture<TransactionReceipt> sendAsyncFuture = this.mapper.addChatContract(contractAddress, user1, user2).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onChatContractAdded(result.getTransactionHash());
            Log.d(TAG, "MAIN_MAPPER :TransactionHash ..... " + result.getTransactionHash());
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        });
    }

    public void getUserChatDetails(String user, BigInteger page, BigInteger pageSize, MapperCallback listen) {
        Log.d(TAG, "MAIN_MAPPER :getUserChatDetails started....");
        CompletableFuture sendAsyncFuture = this.mapper.getUserChatDetails(user, page, pageSize).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onGetUserChatDetails();
            Log.d(TAG, "MAIN_MAPPER :getUserChatDetails ..... " + result);
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable);
            return null;
        });
    }

    public void getChatAddressesForUser(String user, BigInteger page, BigInteger size, MapperCallback listen) {
        Log.d(TAG, "MAIN_MAPPER :getChatAddressesForUser started....");

        CompletableFuture sendAsyncFuture = this.mapper.getChatAddressesForUser(user, page, size).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onGetChatAddressesForUser(result.toString());
            Log.d(TAG, "MAIN_MAPPER :getChatAddressesForUser ..... " + result);
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable);
            return null;
        });
    }

    public void getUserChatDetailsByPhoneNumber(String phoneNumber, BigInteger page, BigInteger pageSize, MapperCallback listen) {
        Log.d(TAG, "MAIN_MAPPER :getUserChatDetailsByPhoneNumber started....");
        CompletableFuture sendAsyncFuture = this.mapper.getUserChatDetailsByPhoneNumber(phoneNumber, page, pageSize).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onGetUserChatDetailsByPhoneNumber();
            Log.d(TAG, "MAIN_MAPPER :getUserChatDetailsByPhoneNumber ..... " + result);
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable);
            return null;
        });
    }

    public void getChatParticipantsForUser(String user, BigInteger page, BigInteger size, MapperCallback listen) {
        Log.d(TAG, "MAIN_MAPPER :getChatParticipantsForUser started....");
        CompletableFuture sendAsyncFuture = this.mapper.getChatParticipantsForUser(user, page, size).sendAsync();
        sendAsyncFuture.thenAccept(result -> {
            listen.onGetChatParticipantsForUser();
            Log.d(TAG, "MAIN_MAPPER :getChatParticipantsForUser ..... " + result);
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable);
            return null;
        });
    }
}
