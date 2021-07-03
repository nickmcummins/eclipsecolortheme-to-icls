package com.nickmcummins.webscraping.xed;

public class Style {
    public StyleName styleName;
    public String foreground;
    public String background;
    public boolean italic;
    public boolean bold;
    public boolean underline;

    Style(StyleName styleName, String foreground, String background, boolean italic, boolean bold, boolean underline) {
        this.styleName = styleName;
        this.foreground = foreground;
        this.background = background;
        this.italic = italic;
        this.bold = bold;
        this.underline = underline;
    }
}
