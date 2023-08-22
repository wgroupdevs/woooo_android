package eu.siacs.conversations.http.model;

public class TextTranslateModel {


    public String text;
    public String languageCode;
    public String serverMsgId;

    public TextTranslateModel(String text, String languageCode, String serverMsgId) {
        this.text = text;
        this.languageCode = languageCode;
        this.serverMsgId = serverMsgId;
    }
}
