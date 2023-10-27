package eu.siacs.conversations.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.Objects;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ItemRowWithNameSelectionBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.http.model.LanguageModel;

public class LanguageAdapter extends ArrayAdapter<LanguageModel> {

    Account mAccount;
    private String TAG = "LanguageAdapter_TAG";

    public LanguageAdapter(@NonNull Context context, ArrayList<LanguageModel> objects, Account mAccount) {
        super(context, 0, objects);
        this.mAccount = mAccount;
        Log.d(TAG, "LanguageAdapter : ACCOUNT " + this.mAccount);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        final LanguageModel language = getItem(position);
        final LanguageAdapter.ViewHolder viewHolder;
        Log.d(TAG, "Language : " + this.mAccount.getLanguage());
        Log.d(TAG, "Language : " + this.mAccount.getLanguageCode());
        Log.d(TAG, "Language : " + language.name);
        Log.d(TAG, "Language : " + language.code);

        if (view == null) {
            ItemRowWithNameSelectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_row_with_name_selection, parent, false);
            view = binding.getRoot();
            viewHolder = new LanguageAdapter.ViewHolder(binding);
            view.setTag(viewHolder);
        } else {
            viewHolder = (LanguageAdapter.ViewHolder) view.getTag();
        }


        viewHolder.binding.itemName.setText(language.name);
        if (Objects.equals(this.mAccount.getLanguageCode(), language.code)) {
            viewHolder.binding.selectedItem.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.selectedItem.setVisibility(View.GONE);

        }

        return view;
    }

    private static class ViewHolder {
        private final ItemRowWithNameSelectionBinding binding;

        private ViewHolder(ItemRowWithNameSelectionBinding binding) {
            this.binding = binding;
        }
    }


}
