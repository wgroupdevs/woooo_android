package com.alphawallet.app.viewmodel;

import android.app.Activity;

import com.alphawallet.app.repository.PreferenceRepositoryType;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionSuccessViewModel extends BaseViewModel {

    private final PreferenceRepositoryType preferenceRepository;

    @Inject
    public TransactionSuccessViewModel(
            PreferenceRepositoryType preferenceRepository
    ) {
        this.preferenceRepository = preferenceRepository;
    }

    public void tryToShowRateAppDialog(Activity context) {
//        RateApp.showRateTheApp(context, preferenceRepository, true);
    }
}
