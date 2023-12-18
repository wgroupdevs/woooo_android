package eu.siacs.conversations.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.FragmentCallLogsBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.entities.Contact;
import eu.siacs.conversations.entities.Conversation;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.ui.adapter.CallLogAdapter;
import eu.siacs.conversations.ui.interfaces.OnConversationSelected;
import eu.siacs.conversations.ui.util.MenuDoubleTabUtil;
import eu.siacs.conversations.ui.util.PresenceSelector;
import eu.siacs.conversations.utils.AccountUtils;
import eu.siacs.conversations.utils.EasyOnboardingInvite;
import eu.siacs.conversations.xml.Namespace;
import eu.siacs.conversations.xmpp.Jid;
import eu.siacs.conversations.xmpp.jingle.RtpCapability;

public class CallLogsFragment extends XmppFragment implements CallLogAdapter.OnAudioClickListener, CallLogAdapter.OnVideoClickListener, CallLogAdapter.OnChatClickListener, CallLogAdapter.OnInfoClickListener {
    private ConversationsActivity activity;
    private FragmentCallLogsBinding binding;
    private CallLogAdapter callLogAdapter;
    private String TAG = "LogsFragment_TAG";
    private List<Message> messageList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ConversationsActivity) {
            this.activity = (ConversationsActivity) activity;
        } else {
            throw new IllegalStateException("Trying to attach fragment to activity that is not an XmppActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_logs, container, false);
        this.binding.fab.setOnClickListener((view) -> StartConversationActivity.launch(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.activity != null && this.activity.xmppConnectionService != null) {
            populateRecyclerView();
        }
    }

    @Override
    void refresh() {
        Log.d(TAG, "refresh called...");

        if (this.activity != null && this.activity.xmppConnectionService != null) {
            populateRecyclerView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackendConnected() {
        if (this.activity != null && this.activity.xmppConnectionService != null) {
            populateRecyclerView();
        }
    }

    private void populateRecyclerView() {
        this.activity.xmppConnectionService.getRTPMessages(this.messageList);
        callLogAdapter = new CallLogAdapter((XmppActivity) getActivity(), this.messageList,false);
        callLogAdapter.setAudioClickListener(this);
        callLogAdapter.setOnVideoClickListener(this);
        callLogAdapter.setOnChatClickListener(this);
        callLogAdapter.setOnInfoClickListener(this);
        binding.callLogsRecyclerView.setAdapter(callLogAdapter);
        this.callLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_conversations_overview, menu);
        AccountUtils.showHideMenuItems(menu);
        final MenuItem easyOnboardInvite = menu.findItem(R.id.action_easy_invite);
        easyOnboardInvite.setVisible(EasyOnboardingInvite.anyHasSupport(activity == null ? null : activity.xmppConnectionService));
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (MenuDoubleTabUtil.shouldIgnoreTap()) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                return true;
            case R.id.action_easy_invite:
//                selectAccountToStartEasyInvite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void triggerRtpSession(Conversation conversation, final String action) {
        if (activity.xmppConnectionService.getJingleConnectionManager().isBusy()) {
            Toast.makeText(getActivity(), R.string.only_one_call_at_a_time, Toast.LENGTH_LONG).show();
            return;
        }
        final Contact contact = conversation.getContact();
        if (contact.getPresences().anySupport(Namespace.JINGLE_MESSAGE)) {
            triggerRtpSession(contact.getAccount(), contact.getJid().asBareJid(), action);
        } else {
            final RtpCapability.Capability capability;
            if (action.equals(RtpSessionActivity.ACTION_MAKE_VIDEO_CALL)) {
                capability = RtpCapability.Capability.VIDEO;
            } else {
                capability = RtpCapability.Capability.AUDIO;
            }
            PresenceSelector.selectFullJidForDirectRtpConnection(activity, contact, capability, fullJid -> {
                triggerRtpSession(contact.getAccount(), fullJid, action);
            });
        }
    }

    private void triggerRtpSession(final Account account, final Jid with, final String action) {
        final Intent intent = new Intent(activity, RtpSessionActivity.class);
        intent.setAction(action);
        intent.putExtra(RtpSessionActivity.EXTRA_ACCOUNT, account.getJid().toEscapedString());
        intent.putExtra(RtpSessionActivity.EXTRA_WITH, with.toEscapedString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onAudioClick(Message message) {
        Conversation conversation = (Conversation) message.getConversation();
        triggerRtpSession(conversation, RtpSessionActivity.ACTION_MAKE_VOICE_CALL);
    }

    @Override
    public void onVideoClick(Message message) {
        Conversation conversation = (Conversation) message.getConversation();
        triggerRtpSession(conversation, RtpSessionActivity.ACTION_MAKE_VIDEO_CALL);
    }

    @Override
    public void onChatClick(Message message) {
        if (activity instanceof OnConversationSelected) {
            Conversation conversation = (Conversation) message.getConversation();
            ((OnConversationSelected) activity).onConversationSelected(conversation);
        } else {
            Log.w(ConversationsOverviewFragment.class.getCanonicalName(), "Activity does not implement OnConversationSelected");
        }

    }

    @Override
    public void onInfoClick(Message message) {
        Conversation conversation = (Conversation) message.getConversation();
        activity.switchToContactDetails(conversation.getContact());
    }
}