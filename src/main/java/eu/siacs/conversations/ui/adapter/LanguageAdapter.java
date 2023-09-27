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

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.LanguageRowBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.http.model.LanguageModel;

public class LanguageAdapter extends ArrayAdapter<LanguageModel> {

    Account mAccount;

    public LanguageAdapter(@NonNull Context context, ArrayList<LanguageModel> objects, Account mAccount) {
        super(context, 0, objects);
        this.mAccount = mAccount;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        final LanguageModel language = getItem(position);
        final LanguageAdapter.ViewHolder viewHolder;
        if (view == null) {
            LanguageRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.language_row, parent, false);
            view = binding.getRoot();
            viewHolder = new LanguageAdapter.ViewHolder(binding);
            view.setTag(viewHolder);
        } else {
            viewHolder = (LanguageAdapter.ViewHolder) view.getTag();
        }

        Log.d("LanguageAdapter","Language : " +this.mAccount.getLanguage());

        viewHolder.binding.languageName.setText(language.name);


        return view;
    }

    private static class ViewHolder {
        private final LanguageRowBinding binding;

        private ViewHolder(LanguageRowBinding binding) {
            this.binding = binding;
        }
    }


}
