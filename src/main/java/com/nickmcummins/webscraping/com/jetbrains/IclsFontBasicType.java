package com.nickmcummins.webscraping.com.jetbrains;

public enum IclsFontBasicType implements IclsFontType {
    NORMAL("0"),
    BOLD("1"),
    ITALIC("2"),
    BOLD_ITALIC("3");

    public final String value;

    IclsFontBasicType(String value) {
        this.value = value;
    }

    public static IclsFontBasicType of(boolean isBold, boolean isItalic) {
        if (isBold) {
            if (isItalic) return BOLD_ITALIC;
            else return BOLD;
        } else
        if (isItalic) return ITALIC;
        else return NORMAL;
    }

    public String toString() {
        return value;
    }
}