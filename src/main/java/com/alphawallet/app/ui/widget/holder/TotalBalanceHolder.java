package com.alphawallet.app.ui.widget.holder;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;

import eu.siacs.conversations.R;

public class TotalBalanceHolder extends BinderViewHolder<BigDecimal> {

    public static final int VIEW_TYPE = 1006;

    private final TextView title;

    public TotalBalanceHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        title = findViewById(R.id.title);
    }

    @Override
    public void bind(@Nullable BigDecimal data, @NonNull Bundle addition) {
        title.setText(data == null
            ? "--"
            : "$" + data.setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString());
    }
}
