package eu.siacs.conversations.http.model;
public class Language {
    public String code;
    public String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

