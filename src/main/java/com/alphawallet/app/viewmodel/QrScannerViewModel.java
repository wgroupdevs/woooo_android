package com.alphawallet.app.viewmodel;

import com.alphawallet.app.service.AnalyticsServiceType;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Created by Ehsan on 01/11/2023.
 */
@HiltViewModel
public class QrScannerViewModel extends BaseViewModel
{

    @Inject
    public QrScannerViewModel(AnalyticsServiceType analyticsService)
    {
        setAnalyticsService(analyticsService);
    }
}
