package eu.siacs.conversations.http.model;
public class LanguageModel {
    public String code;
    public String name;

    public LanguageModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

