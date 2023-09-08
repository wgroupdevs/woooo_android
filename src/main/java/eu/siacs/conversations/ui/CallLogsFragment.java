package eu.siacs.conversations.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.FragmentCallLogsBinding;
import eu.siacs.conversations.ui.util.MenuDoubleTabUtil;
import eu.siacs.conversations.utils.AccountUtils;
import eu.siacs.conversations.utils.EasyOnboardingInvite;

public class CallLogsFragment extends XmppFragment {
    private XmppActivity activity;
    private FragmentCallLogsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof XmppActivity) {
            this.activity = (XmppActivity) activity;
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
    void refresh() {

    }

    @Override
    public void onBackendConnected() {

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
}