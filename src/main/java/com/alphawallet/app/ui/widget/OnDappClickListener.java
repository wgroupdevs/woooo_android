package com.alphawallet.app.ui.widget;

import com.alphawallet.app.entity.DApp;

import java.io.Serializable;

public interface OnDappClickListener extends Serializable {
    void onDappClick(DApp dapp);
}
