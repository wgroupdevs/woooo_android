package eu.siacs.conversations.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ActivityCallLogsDetailBinding;
import eu.siacs.conversations.entities.Contact;
import eu.siacs.conversations.entities.Conversation;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.ui.adapter.CallLogDetailAdapter;
import eu.siacs.conversations.ui.util.AvatarWorkerTask;

public class CallLogsDetailActivity extends XmppActivity {

    private ActivityCallLogsDetailBinding binding;
    public static final String EXTRA_CONVERSATION = "conversationUuid";

    private CallLogDetailAdapter callLogAdapter;
    private Contact contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_logs_detail);


    }


    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {
        if (xmppConnectionService != null) {
            final Intent intent = getIntent();
            if (intent != null) {
                final String uuid = intent.getStringExtra(EXTRA_CONVERSATION);
                Conversation conversation = this.xmppConnectionService.findConversationByUuid(uuid);
                contact = conversation.getContact();
                List<Message> rtpMessages = this.xmppConnectionService.getRTPMessageByConversationUUID(conversation.getUuid());
                callLogAdapter = new CallLogDetailAdapter(this, rtpMessages, false);
                binding.callLogsRecyclerView.setAdapter(callLogAdapter);
                this.callLogAdapter.notifyDataSetChanged();
                AvatarWorkerTask.loadAvatar(contact, binding.contactProfilePic, R.dimen.avatar_on_details_screen_size);

                binding.displayName.setText(contact.getDisplayName());
                binding.phoneNumber.setText(contact.getPhoneNumber());

            }
        }


    }
}