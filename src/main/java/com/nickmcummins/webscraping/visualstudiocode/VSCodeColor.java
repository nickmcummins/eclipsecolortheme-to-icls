package com.nickmcummins.webscraping.visualstudiocode;

public class VSCodeColor {
    private static final char DQUOTE = '"';

    private VSCodeColorSetting setting;
    private String value;

    public VSCodeColor(VSCodeColorSetting setting, String value)
    {
        this.setting = setting;
        this.value = value;
    }

    public String toString() {
        return String.format("%s%s%s: %s#%s%s", DQUOTE, setting, DQUOTE, DQUOTE, value, DQUOTE);
    }
}
