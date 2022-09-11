package com.fredfonseca.bookstoremanager.utils;

import org.springframework.stereotype.Component;

@Component
public class StringPattern {

    public String textPattern(String value) {
        String str = value.replaceAll("[ ]+", " ").trim();
        String filteredString = str
                .replaceAll("[,;]|[;,]", "")
                .replaceAll("[.]+", ".")
                .replaceAll("[;]+", ";")
                .replaceAll("[,]+", ",");
        String[] sentence = filteredString.split(" ");
        sentence[sentence.length-1] = sentence[sentence.length-1]
                .replaceAll("[,]|[;]", "");
        return join(sentence);
    }

    public String basicPattern(String str) {
        return str.replaceAll("[ ]+", " ")
                .replaceAll("[^a-zA-Z0-9'`,. ]+", "")
                .replaceAll("[']+", "'")
                .replaceAll("[`]+", "`")
                .replaceAll("[,]+", ",")
                .replaceAll("[.]+", ".").trim();
    }

    public String onlyWordsPattern(String str) {
        return str.replaceAll("[ ]+", " ")
                .replaceAll("[^a-zA-Z'` ]+", "")
                .replaceAll("[']+", "'")
                .replaceAll("[`]+", "`").trim();
    }

    private String join(String[] sentence) {
        String str = "";
        for(int i = 0 ; i < sentence.length ; i++) {
            str += " "+sentence[i];
        }
        return str.trim();
    }
}
