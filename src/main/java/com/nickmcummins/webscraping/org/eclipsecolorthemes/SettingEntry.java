package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.nodes.Element;

public class SettingEntry
{
    private final Element div;
    public final String name;
    // values
    private String color;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikethrough;

    public SettingEntry(Element div)
    {
        this.div = div;
        this.name = div.select("div[class='setting']").get(0).text();
        this.color = div.select("input").get(0).attr("value");
        this.bold = !div.select("a[class='format-bold']").isEmpty() && div.select("a[class='format-bold']").get(0).attr("href").contains("false");
        this.italic = !div.select("a[class='format-italic-active']").isEmpty() && div.select("a[class='format-italic-active']").get(0).attr("href").contains("false");
        this.underline = !div.select("a[class='format-underline']").isEmpty() && div.select("a[class='format-underline']").get(0).attr("href").contains("false");
        this.strikethrough = !div.select("a[class='format-strikethrough']").isEmpty() && div.select("a[class='format-strikethrough']").get(0).attr("href").contains("false");
    }

    public String toString()
    {
        return String.format("<%s color=\"%s\" />", name, color);
    }
}
