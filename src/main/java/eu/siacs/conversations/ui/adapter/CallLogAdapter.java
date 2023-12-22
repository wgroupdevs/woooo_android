package eu.siacs.conversations.ui.adapter;

// MyAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.expandablelayout.ExpandableLayout;

import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.CallLog;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.entities.RtpSessionStatus;
import eu.siacs.conversations.ui.XmppActivity;
import eu.siacs.conversations.ui.util.AvatarWorkerTask;
import eu.siacs.conversations.utils.TimeFrameUtils;
import eu.siacs.conversations.utils.UIHelper;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private List<CallLog> callLogList;
    private XmppActivity activity;
    private String TAG = "CallLogAdapter_TAG";
    private boolean isFromHomePage = false;

    public CallLogAdapter(XmppActivity activity, List<CallLog> callLogList, boolean isFromHomePage) {
        this.activity = activity;
        this.callLogList = callLogList;
        this.isFromHomePage = isFromHomePage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.call_log_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLog callLog = callLogList.get(position);

        final boolean isDarkTheme = activity.isDarkTheme();
        final boolean received = callLog.getLastMessage().getStatus() <= eu.siacs.conversations.entities.Message.STATUS_RECEIVED;
        final RtpSessionStatus rtpSessionStatus = RtpSessionStatus.of(callLog.getLastMessage().getBody());
        final long duration = rtpSessionStatus.duration;

        if (isFromHomePage) {
            holder.callTime.setText("( " + TimeFrameUtils.resolve(activity, duration) + " ) " + UIHelper.readableTimeDifferenceFull(activity, callLog.getLastMessage().getTimeSent()));
        } else {
            if (received) {
                if (duration > 0) {
                    holder.callTime.setText(activity.getString(R.string.incoming_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration), UIHelper.readableTimeDifferenceFull(activity, callLog.getLastMessage().getTimeSent())));
                } else if (rtpSessionStatus.successful) {
                    holder.callTime.setText(R.string.incoming_call);
                } else {
                    holder.callTime.setText(activity.getString(R.string.missed_call_timestamp, UIHelper.readableTimeDifferenceFull(activity, callLog.getLastMessage().getTimeSent())));
                }
            } else {
                if (duration > 0) {
                    holder.callTime.setText(activity.getString(R.string.outgoing_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration), UIHelper.readableTimeDifferenceFull(activity, callLog.getLastMessage().getTimeSent())));
                } else {
                    holder.callTime.setText(activity.getString(R.string.outgoing_call_timestamp, UIHelper.readableTimeDifferenceFull(activity, callLog.getLastMessage().getTimeSent())));
                }
            }
        }

        AvatarWorkerTask.loadAvatar(callLog.getLastMessage().getContact(), holder.accountImage, R.dimen.avatar);
        holder.callStatusIc.setImageResource(RtpSessionStatus.getDrawable(received, rtpSessionStatus.successful));
        holder.displayName.setText(callLog.getLastMessage().getContact().getDisplayName()+" ("+callLog.getItemCount()+")");

    }

    @Override
    public int getItemCount() {
        return callLogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableLayout expandableLayout;
        ImageView accountImage;
        ImageView callStatusIc;
        ImageView callTypeIc;
        TextView displayName;
        TextView callTime;
        Button audioCallBtn;
        Button videoCallBtn;
        Button chatBtn;
        Button infoBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.call_log_expand_able_layout);
            accountImage = expandableLayout.parentLayout.findViewById(R.id.account_image);
            displayName = expandableLayout.parentLayout.findViewById(R.id.displayName);
            callStatusIc = expandableLayout.parentLayout.findViewById(R.id.call_status_ic);
            callTime = expandableLayout.parentLayout.findViewById(R.id.call_time);
            audioCallBtn = expandableLayout.secondLayout.findViewById(R.id.audio_call_btn);
            videoCallBtn = expandableLayout.secondLayout.findViewById(R.id.video_call_btn);
            chatBtn = expandableLayout.secondLayout.findViewById(R.id.chat_btn);
            infoBtn = expandableLayout.secondLayout.findViewById(R.id.info_call_btn);

            expandableLayout.setOnClickListener(v -> {
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                } else {
                    int position = getBindingAdapterPosition();
                    collapseOtherLayout(position);
                    expandableLayout.expand();
                }
            });


            audioCallBtn.setOnClickListener(v -> {
                if (onAudioCallBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    CallLog callLog = callLogList.get(position);
                    onAudioCallBtnClickListener.onAudioClick(callLog.getLastMessage());
                }
            });

            videoCallBtn.setOnClickListener(v -> {
                if (onVideoCallBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    CallLog callLog = callLogList.get(position);
                    onVideoCallBtnClickListener.onVideoClick(callLog.getLastMessage());
                }
            });

            chatBtn.setOnClickListener(v -> {
                if (onChatBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    CallLog callLog = callLogList.get(position);
                    onChatBtnClickListener.onChatClick(callLog.getLastMessage());
                }
            });

            infoBtn.setOnClickListener(v -> {
                if (onInfoBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    CallLog callLog = callLogList.get(position);
                    onInfoBtnClickListener.onInfoClick(callLog.getLastMessage());
                }
            });


        }


        private void collapseOtherLayout(int position) {

//            for(int index=0; index<messageList.size();index++){
//
//
//            }

        }


    }

    private OnAudioClickListener onAudioCallBtnClickListener;
    private OnVideoClickListener onVideoCallBtnClickListener;
    private OnChatClickListener onChatBtnClickListener;
    private OnInfoClickListener onInfoBtnClickListener;

    public interface OnAudioClickListener {
        void onAudioClick(Message message);
    }

    public interface OnVideoClickListener {
        void onVideoClick(Message message);
    }

    public interface OnChatClickListener {
        void onChatClick(Message message);
    }

    public interface OnInfoClickListener {
        void onInfoClick(Message message);
    }

    public void setAudioClickListener(OnAudioClickListener listener) {
        this.onAudioCallBtnClickListener = listener;
    }

    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoCallBtnClickListener = listener;
    }

    public void setOnChatClickListener(OnChatClickListener listener) {
        this.onChatBtnClickListener = listener;
    }

    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.onInfoBtnClickListener = listener;
    }
}
