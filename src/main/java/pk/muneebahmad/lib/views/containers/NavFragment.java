package pk.muneebahmad.lib.views.containers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.annotation.Nullable;

import eu.siacs.conversations.xmpp.jingle.stanzas.Content;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:39 am 05/1/2020
 * <code>class</code> LibFragment.java
 */
public abstract class NavFragment {

    private static final String TAG = NavFragment.class.getSimpleName() + ".java";
    private Activity mActivity;

    /**
     *
     * @param activity
     */
    public NavFragment(@NonNull Activity activity) {
       this.mActivity = activity;
    }

    /**
     *
     * @param inflater
     * @param root
     * @return
     */
    public abstract View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root);

    /**
     *
     * @param view
     */
    public abstract  <T extends View> void viewCreated(@NonNull T view);

    /**
     *
     * @return
     */
    public Activity getActivity() {
        return this.mActivity;
    }

    /**
     *
     * @return
     */
    public Context getContext() {
        return this.mActivity;
    }

    /**
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }

} /** end class. */
