package com.nickmcummins.webscraping.com.jetbrains;

public enum IclsFontEffectType implements IclsFontType {
    UNDERLINE("1"),
    UNDERWAVE("2"),
    STRIKETHROUGH("3");

    public final String value;

    IclsFontEffectType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}