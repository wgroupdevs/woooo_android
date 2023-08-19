package eu.siacs.conversations.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import eu.siacs.conversations.R;
import eu.siacs.conversations.http.model.Language;

public class CustomDialogUtil {

    public static void showCustomDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.language_dialog, null);
        ListView languagesListView;
        languagesListView = customView.findViewById(R.id.languagesListView);
        ArrayList<Language> languageList = parseJSON(context);
        ArrayAdapter<Language> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, languageList);
        languagesListView.setAdapter(adapter);
        languagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.hide();
                // Display a message
                Toast.makeText(context, "You clicked on " + languageList.get(position).code, Toast.LENGTH_SHORT).show();
            }
        });

        // Set the custom layout to the dialog
        alertDialogBuilder.setView(customView);

        alertDialog.show();
    }

    private static ArrayList<Language> parseJSON(Context context) {
        ArrayList<Language> languageList = new ArrayList<>();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("languages.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray languagesArray = jsonObject.getJSONArray("languages");

            for (int i = 0; i < languagesArray.length(); i++) {
                JSONObject languageObject = languagesArray.getJSONObject(i);
                String code = languageObject.getString("code");
                String name = languageObject.getString("name");
                Language language = new Language(code, name);
                languageList.add(language);
                Log.d(String.valueOf(+languageList.size()), "qwdqwdqwdqw");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return languageList;
    }

}