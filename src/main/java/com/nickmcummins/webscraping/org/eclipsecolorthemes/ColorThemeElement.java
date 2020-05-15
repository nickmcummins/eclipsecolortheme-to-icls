package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ColorThemeElement {
    public final String name;
    // values
    private final String colorValue;
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;
    private final boolean strikethrough;

    public ColorThemeElement(String name, String colorValue, boolean bold, boolean italic, boolean underline, boolean strikethrough) {
        this.name = name;
        this.colorValue = colorValue;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
    }

    public static ColorThemeElement fromHtmlPageDiv(Element div) {
        return new ColorThemeElement(
                div.select("div[class='setting']").get(0).text(),
                div.select("input").get(0).attr("value"),
                !div.select("a[class='format-bold-active']").isEmpty() && div.select("a[class='format-bold-active']").get(0).attr("href").contains("false"),
                !div.select("a[class='format-italic-active']").isEmpty() && div.select("a[class='format-italic-active']").get(0).attr("href").contains("false"),
                !div.select("a[class='format-underline-active']").isEmpty() && div.select("a[class='format-underline-active']").get(0).attr("href").contains("false"),
                !div.select("a[class='format-strikethrough-active']").isEmpty() && div.select("a[class='format-strikethrough-active']").get(0).attr("href").contains("false")
        );
    }

    public static ColorThemeElement fromXmlElement(Element element) {
        String tagName = element.tagName();
        Attributes attributes = element.attributes();
        return new ColorThemeElement(
                tagName,
                attributes.get("color"),
                attributes.hasKey("bold") && Boolean.parseBoolean(attributes.get("bold")),
                attributes.hasKey("italic") && Boolean.parseBoolean(attributes.get("italic")),
                attributes.hasKey("underline") && Boolean.parseBoolean(attributes.get("underline")),
                attributes.hasKey("strikethrough") && Boolean.parseBoolean(attributes.get("strikethrough"))
        );
    }

    public String toString() {
        List<String> fontProperties = new ArrayList<>(4);
        if (bold)
            fontProperties.add("bold=\"true\"");
        if (italic)
            fontProperties.add("italic=\"true\"");
        if (underline)
            fontProperties.add("underline=\"true\"");
        if (strikethrough)
            fontProperties.add("strikethrough=\"true\"");

        if (!fontProperties.isEmpty())
            fontProperties.add("");

        return String.format("<%s color=\"%s\" %s/>", name, colorValue, String.join(" ", fontProperties));
    }

    public String getColorValue() {
        return colorValue;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }
}
