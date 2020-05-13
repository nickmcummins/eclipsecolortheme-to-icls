package com.nickmcummins.webscraping;

public enum SchemeType {
    LIGHT("light"),
    DARK("dark");

    private final String type;

    SchemeType(String type) {
        this.type = type;
    }
}
