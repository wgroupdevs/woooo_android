package eu.siacs.conversations.blockchain.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class ChatContract extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ADDCHATMESSAGE = "addChatMessage";

    public static final String FUNC_BLOCKUSER = "blockUser";

    public static final String FUNC_CHECKBLOCK = "checkBlock";

    public static final String FUNC_CHECKBLOCKEDUSER = "checkBlockedUser";

    public static final String FUNC_GETCHATHISTORY = "getChatHistory";

    public static final String FUNC_GETMAXRECENTMESSAGES = "getMaxRecentMessages";

    public static final String FUNC_GETTOTALMESSAGES = "getTotalMessages";

    public static final String FUNC_RECENTCHAT = "recentChat";

    public static final String FUNC_SETMAXRECENTMESSAGES = "setMaxRecentMessages";

    public static final String FUNC_UNBLOCKUSER = "unblockUser";

    @Deprecated
    protected ChatContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChatContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ChatContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ChatContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addChatMessage(String _user1, String _user2, String userMessage, Boolean ismedia) {
        final Function function = new Function(
                FUNC_ADDCHATMESSAGE, 
                Arrays.<Type>asList(new Address(160, _user1),
                new Address(160, _user2),
                new Utf8String(userMessage),
                new Bool(ismedia)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> blockUser(String userToBeBlocked, String blockedByUser) {
        final Function function = new Function(
                FUNC_BLOCKUSER, 
                Arrays.<Type>asList(new Address(160, userToBeBlocked),
                new Address(160, blockedByUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> checkBlock() {
        final Function function = new Function(FUNC_CHECKBLOCK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> checkBlockedUser() {
        final Function function = new Function(FUNC_CHECKBLOCKEDUSER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getChatHistory(BigInteger page, BigInteger size) {
        final Function function = new Function(FUNC_GETCHATHISTORY, 
                Arrays.<Type>asList(new Uint256(page),
                new Uint256(size)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ChatMessage>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getMaxRecentMessages() {
        final Function function = new Function(FUNC_GETMAXRECENTMESSAGES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalMessages() {
        final Function function = new Function(FUNC_GETTOTALMESSAGES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> recentChat() {
        final Function function = new Function(FUNC_RECENTCHAT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ChatMessage>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setMaxRecentMessages(BigInteger limit) {
        final Function function = new Function(
                FUNC_SETMAXRECENTMESSAGES, 
                Arrays.<Type>asList(new Uint256(limit)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> unblockUser(String userToBeUnBlocked) {
        final Function function = new Function(
                FUNC_UNBLOCKUSER, 
                Arrays.<Type>asList(new Address(160, userToBeUnBlocked)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ChatContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChatContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ChatContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChatContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ChatContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ChatContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ChatContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ChatContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class ChatMessage extends DynamicStruct {
        public String U1;

        public String U2;

        public String message;

        public BigInteger timestamp;

        public Boolean isMedia;

        public ChatMessage(String U1, String U2, String message, BigInteger timestamp, Boolean isMedia) {
            super(new Address(160, U1),
                    new Address(160, U2),
                    new Utf8String(message),
                    new Uint256(timestamp),
                    new Bool(isMedia));
            this.U1 = U1;
            this.U2 = U2;
            this.message = message;
            this.timestamp = timestamp;
            this.isMedia = isMedia;
        }

        public ChatMessage(Address U1, Address U2, Utf8String message, Uint256 timestamp, Bool isMedia) {
            super(U1, U2, message, timestamp, isMedia);
            this.U1 = U1.getValue();
            this.U2 = U2.getValue();
            this.message = message.getValue();
            this.timestamp = timestamp.getValue();
            this.isMedia = isMedia.getValue();
        }
    }
}
