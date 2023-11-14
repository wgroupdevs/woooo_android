package com.woooapp.meeting.impl.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.woooapp.meeting.impl.views.models.Member;
import com.woooapp.meeting.lib.MeetingClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:44 am 21/10/2023
 * <code>class</code> MemberAdapter.java
 */
public class MemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<Member> members = new LinkedList<>();

    public MemberAdapter(Context mContext, List<Member> members) {
        this.mContext = mContext;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_list_members, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.ivThumb = convertView.findViewById(R.id.ivThumb);
        vh.tvName = convertView.findViewById(R.id.tvName);
        vh.tvRole = convertView.findViewById(R.id.tvRole);
//        vh.buttonMute = convertView.findViewById(R.id.buttonMute);
//        vh.buttonCam = convertView.findViewById(R.id.buttonCam);
//        vh.buttonKickout = convertView.findViewById(R.id.buttonKickout);

        vh.tvName.setText(members.get(position).getName());
        vh.tvRole.setText(members.get(position).getRole() == MeetingClient.Role.ADMIN ? "ADMIN" : "MEMBER");
        MeetingClient.Role role = members.get(position).getRole();
        if (role == MeetingClient.Role.ADMIN) {
            vh.tvRole.setBackgroundResource(R.drawable.bg_red_stroke);
            vh.tvRole.setTextColor(Color.parseColor("#ffd700"));
//            vh.buttonMute.setVisibility(View.GONE);
//            vh.buttonCam.setVisibility(View.GONE);
//            vh.buttonKickout.setVisibility(View.GONE);
        } else {
            vh.tvRole.setVisibility(View.GONE);
        }
        if (members.get(position).getPicture() != null) {
            try {
                URL url = new URL(members.get(position).getPicture());
                ViewHolder finalVh = vh;
                Http.build().getImage((Activity) mContext, true, members.get(position).getPicture(), new HttpImageAdapter() {
                    @Override
                    public void connected(String resource) {

                    }

                    @Override
                    public void failed(String resource, String reasonPhrase) {

                    }

                    @Override
                    public void done(String resource, Bitmap bitmap) {
                        ((Activity) mContext).runOnUiThread(() -> {
                            try {
                                CircleDrawable cd = new CircleDrawable(bitmap);
                                finalVh.ivThumb.setImageDrawable(cd);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivThumb;
        TextView tvName;
        TextView tvRole;
//        LinearLayout buttonMute;
//        LinearLayout buttonCam;
//        LinearLayout buttonKickout;
    }

} /**
 * end class.
 */
