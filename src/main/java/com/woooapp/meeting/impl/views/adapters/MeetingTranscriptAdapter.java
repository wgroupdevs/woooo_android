package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.views.models.Transcript;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.util.DateUtils;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:23 pm 12/10/2023
 * <code>class</code> MeetingTranscriptAdapter.java
 */
public class MeetingTranscriptAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Transcript> transcriptList = new LinkedList<>();

    /**
     *
     * @param context
     */
    public MeetingTranscriptAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return transcriptList.size();
    }

    @Override
    public Object getItem(int position) {
        return transcriptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_transcript_bubble, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvOriginal = convertView.findViewById(R.id.tvTranscriptOriginal);
        vh.tvMessage = convertView.findViewById(R.id.tvTranscriptMessage);
        vh.tvTime = convertView.findViewById(R.id.tvTranscriptTime);
        vh.tvName = convertView.findViewById(R.id.tvTranscriptName);

        if (transcriptList.get(position).getTranslation() != null) {
            vh.tvMessage.setText(transcriptList.get(position).getTranslation());
        }
        if (transcriptList.get(position).getOriginal() != null) {
            vh.tvOriginal.setText(transcriptList.get(position).getOriginal());
        }
        vh.tvTime.setText(DateUtils.getCurrentTime());
        if (transcriptList.get(position).getSenderName() != null) {
            vh.tvName.setText(transcriptList.get(position).getSenderName());
        } else {
            vh.tvName.setText("");
        }

        return convertView;
    }

    /**
     *
     * @param list
     */
    public void replaceList(List<Transcript> list) {
        this.transcriptList = list;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvOriginal;
        TextView tvMessage;
        TextView tvTime;
        TextView tvName;
    } // end class

} /** end class. */
