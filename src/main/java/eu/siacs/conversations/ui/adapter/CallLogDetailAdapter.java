package eu.siacs.conversations.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.entities.RtpSessionStatus;
import eu.siacs.conversations.ui.XmppActivity;
import eu.siacs.conversations.utils.TimeFrameUtils;
import eu.siacs.conversations.utils.UIHelper;

public class CallLogDetailAdapter extends RecyclerView.Adapter<CallLogDetailAdapter.ViewHolder> {

    private List<Message> rtpMessages;
    private XmppActivity activity;
    private String TAG = "CallLogAdapter_TAG";
    private boolean isFromHomePage = false;

    public CallLogDetailAdapter(XmppActivity activity, List<Message> rtpMessages, boolean isFromHomePage) {
        this.activity = activity;
        this.rtpMessages = rtpMessages;
        this.isFromHomePage = isFromHomePage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.call_log_detail_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = rtpMessages.get(position);

        final boolean received = message.getStatus() <= eu.siacs.conversations.entities.Message.STATUS_RECEIVED;
        final RtpSessionStatus rtpSessionStatus = RtpSessionStatus.of(message.getBody());
        final long duration = rtpSessionStatus.duration;
        holder.callStatusIc.setColorFilter(ContextCompat.getColor(activity, R.color.black));

        if (received) {
            if (duration > 0) {
                holder.dateTime.setText(UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent()));
                holder.duration.setText(activity.getString(R.string.incoming_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration),""));
            } else if (rtpSessionStatus.successful) {
                holder.duration.setText(R.string.incoming_call);
            } else {
                holder.dateTime.setText(UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent()));
                holder.duration.setText(activity.getString(R.string.missed_call_timestamp,""));
                holder.callStatusIc.setColorFilter(ContextCompat.getColor(activity, R.color.red800));

            }
        } else {
            if (duration > 0) {
                holder.dateTime.setText(UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent()));

                holder.duration.setText(activity.getString(R.string.outgoing_call_duration_timestamp, TimeFrameUtils.resolve(activity, duration), ""));
            } else {
                holder.dateTime.setText(UIHelper.readableTimeDifferenceFull(activity, message.getTimeSent()));
                holder.duration.setText(activity.getString(R.string.outgoing_call_timestamp, TimeFrameUtils.resolve(activity, duration)));
                holder.callStatusIc.setColorFilter(ContextCompat.getColor(activity, R.color.cyan));
            }
        }
        holder.callStatusIc.setImageResource(RtpSessionStatus.getDrawable(received, rtpSessionStatus.successful));

    }

    @Override
    public int getItemCount() {
        return rtpMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView callStatusIc;
        ImageView callTypeIc;
        TextView dateTime;
        TextView duration;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callStatusIc = itemView.findViewById(R.id.call_status_ic);
            dateTime = itemView.findViewById(R.id.date_time);
            duration = itemView.findViewById(R.id.call_duration);

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
