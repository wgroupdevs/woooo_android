package eu.siacs.conversations.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eu.siacs.conversations.ui.util.SettingsUtils;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent receivedIntent;
        final Intent newIntent = new Intent(this, ConversationsActivity.class);

        if (savedInstanceState == null) {
            receivedIntent = getIntent();
        } else {
            receivedIntent = savedInstanceState.getParcelable("intent");
        }

        if (receivedIntent != null) {
            int showCall = receivedIntent.getIntExtra(ConversationsActivity.EXTRA_CIRCLE_MENU_INDEX, -1);
            newIntent.putExtra(ConversationsActivity.EXTRA_CIRCLE_MENU_INDEX, showCall);
            Log.d("ConActivity_Tag", "SHOW_CALL : " + showCall);
        }

        startActivity(newIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsUtils.applyScreenshotPreventionSetting(this);
    }
}
