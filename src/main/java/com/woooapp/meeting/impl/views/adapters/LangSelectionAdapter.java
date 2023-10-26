package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.woooapp.meeting.impl.views.models.Languages;

import java.util.ArrayList;
import java.util.List;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 9:50 am 13/10/2023
 * <code>class</code> LangSelectionAdapter.java
 */
public class LangSelectionAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Languages.Language> languageList;
    private String selectedLanguageName;
    /**
     *
     * @param context
     * @param languageList
     * @param selectedLanguageName
     */
    public LangSelectionAdapter(@NonNull Context context,
                                @NonNull List<Languages.Language> languageList,
                                @Nullable String selectedLanguageName) {
       this.mContext = context;
       this.languageList = languageList;
       this.selectedLanguageName = selectedLanguageName;
    }

    @Override
    public int getCount() {
        return this.languageList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.languageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_list_select_language, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvName = convertView.findViewById(R.id.tvLangName);
        vh.tvName.setText(languageList.get(position).getName());
        vh.ivCheck = convertView.findViewById(R.id.ivLangCheck);
        if (selectedLanguageName != null) {
            if (languageList.get(position).getName().equals(selectedLanguageName)) {
                vh.ivCheck.setVisibility(View.VISIBLE);
            } else {
                vh.ivCheck.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        ImageView ivCheck;
    }

} /** end class. */
