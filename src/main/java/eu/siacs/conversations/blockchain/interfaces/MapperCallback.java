package eu.siacs.conversations.blockchain.interfaces;

public interface MapperCallback {
    void onChatContractAdded(String address);

    void onGetChatAddressesForUser(String address);

    void onGetUserChatDetails();

    void onGetChatParticipantsForUser();

    void onGetUserChatDetailsByPhoneNumber();
}
