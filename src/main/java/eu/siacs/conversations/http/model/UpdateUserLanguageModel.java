package eu.siacs.conversations.http.model;

import androidx.datastore.preferences.protobuf.Any;

public class UpdateUserLanguageModel {
    public boolean Success = false;
    public String Message = "";
    public String Error = "";
    public Any Data = null;
}
