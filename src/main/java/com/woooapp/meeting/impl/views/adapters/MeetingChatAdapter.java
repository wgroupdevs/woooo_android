package com.woooapp.meeting.impl.views.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.utils.WDirector;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.models.Chat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;
import pk.muneebahmad.lib.util.DateUtils;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:18 pm 06/10/2023
 * <code>class</code> MeetingChatAdapter.java
 */
public class MeetingChatAdapter extends BaseAdapter {

    private final Activity mContext;
    private List<Chat> chatList = new LinkedList<>();

    /**
     *
     * @param mContext
     * @param chatList
     */
    public MeetingChatAdapter(@NonNull Activity mContext, @NonNull List<Chat> chatList) {
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
    public int getViewTypeCount() {
        return 2;
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
            svh.tvTime.setText(DateUtils.getCurrentTime());
            WooAnimationUtil.showView(view, null);

            return view;
        } else if (getItemViewType(i) == Chat.MESSAGE_TYPE_RECV) {
            RecvViewHolder rvh = null;
            if (view == null) {
                rvh = new RecvViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.meeting_chat_meesage_recv, null);
                view.setTag(rvh);
            } else {
                rvh = (RecvViewHolder) view.getTag();
            }

            rvh.tvName = view.findViewById(R.id.meetingMsgRecvTvName);
            rvh.tvName.setText(chatList.get(i).getMessage().getName());
            rvh.tvMessage = view.findViewById(R.id.meetingMsgRecvTvMsg);
            rvh.tvMessage.setMaxWidth(250);
            rvh.tvMessage.setText(chatList.get(i).getMessage().getMessage());
            rvh.tvTime = view.findViewById(R.id.meetingChatRecvTimeTv);
            rvh.tvTime.setText(DateUtils.getCurrentTime());
            rvh.ivThumb = view.findViewById(R.id.meetingMsgRecvIvThumb);
            rvh.tvTranslation = view.findViewById(R.id.meetingMsgRecvTransTv);

            if (WDirector.getInstance().isChatTranslationEnabled()) {
                if (chatList.get(i).getTranslation() != null) {
                    rvh.tvTranslation.setVisibility(View.VISIBLE);
                    rvh.tvTranslation.setText(chatList.get(i).getTranslation());
                }
            } else {
                rvh.tvTranslation.setVisibility(View.GONE);
            }

            String u = chatList.get(i).getMessage().getProfileImage();
            if (u != null) {
                try {
                    URL url = new URL(u);
                    RecvViewHolder finalRvh = rvh;
                    Http.build().getImage(mContext, true, url.toString(), new HttpImageAdapter() {
                        @Override
                        public void connected(String resource) {
                        }

                        @Override
                        public void failed(String resource, String reasonPhrase) {
                        }

                        @Override
                        public void done(String resource, Bitmap bitmap) {
                            if (bitmap != null) {
                                mContext.runOnUiThread(() -> {
                                    CircleDrawable cd = new CircleDrawable(bitmap);
                                    finalRvh.ivThumb.setImageDrawable(cd);
                                });
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    RecvViewHolder finalRvh1 = rvh;
                    mContext.runOnUiThread(() -> {
                        finalRvh1.ivThumb.setImageResource(R.drawable.buddy2);
                    });
                }
            }
            WooAnimationUtil.showView(view, null);
            return view;
        }
        return null;
    }

    static class SentViewHolder {
        TextView tvMessage;
        TextView tvTime;
    }

    static class RecvViewHolder {
        TextView tvName;
        TextView tvMessage;
        TextView tvTime;
        TextView tvTranslation;
        ImageView ivThumb;
    }

} /** end class. */
