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
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.entities.RtpSessionStatus;
import eu.siacs.conversations.ui.XmppActivity;
import eu.siacs.conversations.ui.util.AvatarWorkerTask;
import eu.siacs.conversations.utils.TimeFrameUtils;
import eu.siacs.conversations.utils.UIHelper;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private List<Message> messageList;
    private XmppActivity activity;
    private String TAG = "CallLogAdapter_TAG";
    private boolean isFromHomePage = false;

    public CallLogAdapter(XmppActivity activity, List<Message> messageList, boolean isFromHomePage) {
        this.activity = activity;
        this.messageList = messageList;
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
        Message message = messageList.get(position);

        final boolean isDarkTheme = activity.isDarkTheme();
        final boolean received = message.getStatus() <= eu.siacs.conversations.entities.Message.STATUS_RECEIVED;
        final RtpSessionStatus rtpSessionStatus = RtpSessionStatus.of(message.getBody());
        final long duration = rtpSessionStatus.duration;

        if (isFromHomePage) {
            holder.callTime.setText("( " +TimeFrameUtils.resolve(activity, duration)+" ) "+UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent()));
        } else {
            if (received) {
                if (duration > 0) {
                    holder.callTime.setText(activity.getString(R.string.incoming_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration), UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent())));
                } else if (rtpSessionStatus.successful) {
                    holder.callTime.setText(R.string.incoming_call);
                } else {
                    holder.callTime.setText(activity.getString(R.string.missed_call_timestamp, UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent())));
                }
            } else {
                if (duration > 0) {
                    holder.callTime.setText(activity.getString(R.string.outgoing_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration), UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent())));
                } else {
                    holder.callTime.setText(activity.getString(R.string.outgoing_call_timestamp, UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent())));
                }
            }
        }

        AvatarWorkerTask.loadAvatar(message.getContact(), holder.accountImage, R.dimen.avatar);
        holder.callStatusIc.setImageResource(RtpSessionStatus.getDrawable(received, rtpSessionStatus.successful, isDarkTheme));
        holder.displayName.setText(message.getContact().getDisplayName());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
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
                    Message message = messageList.get(position);
                    onAudioCallBtnClickListener.onAudioClick(message);
                }
            });

            videoCallBtn.setOnClickListener(v -> {
                if (onVideoCallBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    Message message = messageList.get(position);
                    onVideoCallBtnClickListener.onVideoClick(message);
                }
            });

            chatBtn.setOnClickListener(v -> {
                if (onChatBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    Message message = messageList.get(position);
                    onChatBtnClickListener.onChatClick(message);
                }
            });

            infoBtn.setOnClickListener(v -> {
                if (onInfoBtnClickListener != null) {
                    int position = getBindingAdapterPosition();
                    Message message = messageList.get(position);
                    onInfoBtnClickListener.onInfoClick(message);
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
