package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.models.Chat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:18 pm 06/10/2023
 * <code>class</code> MeetingChatAdapter.java
 */
public class MeetingChatAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Chat> chatList = new LinkedList<>();

    /**
     *
     * @param mContext
     * @param chatList
     */
    public MeetingChatAdapter(@NonNull Context mContext, @NonNull List<Chat> chatList) {
        this.mContext = mContext;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).getMessageType();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == Chat.MESSAGE_TYPE_SENT) {
            SentViewHolder svh = null;
            if (view == null) {
                svh = new SentViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.meeting_chat_message_send, null);
                view.setTag(svh);
            } else {
                svh = (SentViewHolder) view.getTag();
            }

            svh.tvMessage = view.findViewById(R.id.meetingMsgSendTvMsg);
            svh.tvMessage.setMaxWidth(250);
            svh.tvMessage.setText(chatList.get(i).getMessage().getMessage());
            svh.tvTime = view.findViewById(R.id.meetingChatTimeTv);
            svh.tvTime.setText("02:30 pm");
            WooAnimationUtil.showView(view, null);

            return view;
        }
        RecvViewHolder rvh = null;
        if (view == null) {
            rvh = new RecvViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.meeting_chat_meesage_recv, null);
            view.setTag(rvh);
        } else {
            rvh = (RecvViewHolder) view.getTag();
        }

        rvh.tvMessage = view.findViewById(R.id.meetingMsgRecvTvMsg);
        rvh.tvMessage.setMaxWidth(250);
        rvh.tvMessage.setText(chatList.get(i).getMessage().getMessage());
        rvh.tvTime = view.findViewById(R.id.meetingChatRecvTimeTv);
        rvh.tvTime.setText("2:30 pm");
        rvh.ivThumb = view.findViewById(R.id.meetingMsgRecvIvThumb);
        String u = chatList.get(i).getMessage().getProfileImage();
        if (u != null) {
            try {
                URL url = new URL(u);
                RecvViewHolder finalRvh = rvh;
                Http.build().getImage(mContext, true, url.toString(), new HttpImageAdapter() {
                    @Override
                    public void connected(String resource) {}

                    @Override
                    public void failed(String resource, String reasonPhrase) {}

                    @Override
                    public void done(String resource, Bitmap bitmap) {
                        if (bitmap != null) {
                            CircleDrawable cd = new CircleDrawable(bitmap);
                            finalRvh.ivThumb.setImageDrawable(cd);
                        }
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        WooAnimationUtil.showView(view, null);
        return view;
    }

    static class SentViewHolder {
        TextView tvMessage;
        TextView tvTime;
    }

    static class RecvViewHolder {
        TextView tvMessage;
        TextView tvTime;
        ImageView ivThumb;
    }

} /** end class. */
