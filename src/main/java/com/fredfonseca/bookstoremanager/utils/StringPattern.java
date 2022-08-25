package com.fredfonseca.bookstoremanager.utils;

public class StringPattern {

    public String textPattern(String value) {
        String str = value.replaceAll("[ ]+", " ").trim();
        String filteredString = str
                .replaceAll(",;", "")
                .replaceAll(";,", "")
                .replaceAll(".+", ".")
                .replaceAll(";+", ";")
                .replaceAll(",+", ",");
        String[] sentence = filteredString.split(" ");
        sentence[sentence.length-1] = sentence[sentence.length-1]
                .replaceAll(",", "")
                .replaceAll(";", "");
        return join(sentence);
    }

    private String join(String[] sentence) {
        String str = "";
        for(int i = 0 ; i < sentence.length ; i++) {
            str += " "+sentence[i];
        }
        return str.trim();
    }
}
