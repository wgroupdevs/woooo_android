package com.alphawallet.app.widget;

import static com.alphawallet.app.entity.WalletPage.ACTIVITY;
import static com.alphawallet.app.entity.WalletPage.SETTINGS;
import static com.alphawallet.app.entity.WalletPage.WALLET;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alphawallet.app.entity.WalletPage;

import java.util.ArrayList;

import eu.siacs.conversations.R;

public class AWalletBottomNavigationView extends LinearLayout
{
    private final TextView walletLabel;
    private final TextView settingsBadge;
    private final TextView settingsLabel;
    private final RelativeLayout settingsTab;
    private final TextView activityLabel;
    private final ArrayList<String> settingsBadgeKeys = new ArrayList<>();
    private OnBottomNavigationItemSelectedListener listener;
    private WalletPage selectedItem;

    public AWalletBottomNavigationView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.layout_bottom_navigation, this);
        walletLabel = findViewById(R.id.nav_wallet_text);
        activityLabel = findViewById(R.id.nav_activity_text);
        settingsTab = findViewById(R.id.settings_tab);
        settingsLabel = findViewById(R.id.nav_settings_text);
        settingsBadge = findViewById(R.id.settings_badge);

        walletLabel.setOnClickListener(v -> selectItem(WALLET));
        activityLabel.setOnClickListener(v -> selectItem(ACTIVITY));
        settingsTab.setOnClickListener(v -> selectItem(SETTINGS));


        // set wallet fragment selected on start
        setSelectedItem(WALLET);
    }

    public void setListener(OnBottomNavigationItemSelectedListener listener)
    {
        this.listener = listener;
    }

    private void selectItem(WalletPage index)
    {
        listener.onBottomNavigationItemSelected(index);
    }

    public WalletPage getSelectedItem()
    {
        return selectedItem;
    }

    public void setSelectedItem(WalletPage index)
    {
        deselectAll();
        selectedItem = index;
        switch (index)
        {

            case WALLET:
                walletLabel.setSelected(true);
                walletLabel.setTextAppearance(R.style.TextAppearance_Conversations_Subhead);
                break;
            case SETTINGS:
                settingsLabel.setSelected(true);
                settingsLabel.setTextAppearance(R.style.TextAppearance_Conversations_Subhead);
                break;
            case ACTIVITY:
                activityLabel.setSelected(true);
                activityLabel.setTextAppearance(R.style.TextAppearance_Conversations_Subhead);

//                activityLabel.setTypeface(semiboldTypeface);
                break;
        }
    }

    private void deselectAll()
    {
        walletLabel.setSelected(false);
        walletLabel.setTextAppearance(R.style.TextAppearance_Conversations_Body1);
        settingsLabel.setSelected(false);
        settingsLabel.setTextAppearance(R.style.TextAppearance_Conversations_Body1);
        activityLabel.setSelected(false);
        activityLabel.setTextAppearance(R.style.TextAppearance_Conversations_Body1);    }

    public void setSettingsBadgeCount(int count)
    {
        if (count > 0)
        {
            settingsBadge.setVisibility(View.VISIBLE);
        }
        else
        {
            settingsBadge.setVisibility(View.GONE);
        }
        settingsBadge.setText(String.valueOf(count));
    }

    public void addSettingsBadgeKey(String key)
    {
        if (!settingsBadgeKeys.contains(key))
        {
            settingsBadgeKeys.add(key);
        }
        showOrHideSettingsBadge();
    }

    public void removeSettingsBadgeKey(String key)
    {
        settingsBadgeKeys.remove(key);
        showOrHideSettingsBadge();
    }

    private void showOrHideSettingsBadge()
    {
        if (settingsBadgeKeys.size() > 0)
        {
            settingsBadge.setVisibility(View.VISIBLE);
        }
        else
        {
            settingsBadge.setVisibility(View.GONE);
        }
        settingsBadge.setText(String.valueOf(settingsBadgeKeys.size()));
    }

    public interface OnBottomNavigationItemSelectedListener
    {
        boolean onBottomNavigationItemSelected(WalletPage index);
    }
}
