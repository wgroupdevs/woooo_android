package eu.siacs.conversations.blockchain.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import eu.siacs.conversations.blockchain.contracts.AddressMapper;

@HiltViewModel
public class AddressMapperViewModel extends ViewModel {

    private String TAG = "AddressMapper_TAG";
    private final AddressMapper addressMapper;

    @Inject
    AddressMapperViewModel(AddressMapper preferenceRepository) {
        this.addressMapper = preferenceRepository;
    }


    public void checkAddressMapper() throws ExecutionException, InterruptedException {
        Log.d(TAG, "ADDRESS_MAPPER : " + this.addressMapper.getOperatorContract().sendAsync().get());
    }


    public void setAddressPhoneNumber(String userAddress, String phoneNumber) {


    }


}
