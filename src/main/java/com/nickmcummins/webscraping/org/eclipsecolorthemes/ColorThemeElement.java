package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ColorThemeElement {
    public final String name;
    // values
    private final String colorValue;
    private final Boolean bold;
    private final Boolean italic;
    private final Boolean underline;
    private final Boolean strikethrough;

    public ColorThemeElement(String name, String colorValue, Boolean bold, Boolean italic, Boolean underline, Boolean strikethrough) {
        this.name = name;
        this.colorValue = colorValue;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
    }

    private static Boolean fontTypeFromHtmlElementAttr(Element div, IntellijIdeaColorScheme.FontType fontType) {
        String elementClassSelector = String.format("a[class='%s-active']", fontType.name().toLowerCase());

        Elements aElements = div.select(elementClassSelector);
        if (!aElements.isEmpty()) {
            return aElements.get(0).attr("href").contains("false");
        }

        return null;
    }

    public static ColorThemeElement fromHtmlPageDiv(Element div) {
        return new ColorThemeElement(
                div.select("div[class='setting']").get(0).text(),
                div.select("input").get(0).attr("value"),
                fontTypeFromHtmlElementAttr(div, IntellijIdeaColorScheme.FontBasicType.BOLD),
                fontTypeFromHtmlElementAttr(div, IntellijIdeaColorScheme.FontBasicType.ITALIC),
                fontTypeFromHtmlElementAttr(div, IntellijIdeaColorScheme.FontEffectType.UNDERLINE),
                fontTypeFromHtmlElementAttr(div, IntellijIdeaColorScheme.FontEffectType.STRIKETHROUGH)
        );
    }

    private static Boolean fontTypeFromXmlElementAttr(Attributes attributes, IntellijIdeaColorScheme.FontType fontType) {
        String attr = fontType.name().toLowerCase();
        if (attributes.hasKey(attr)) {
            return Boolean.parseBoolean(attributes.get(attr));
        }

        return null;
    }

    public static ColorThemeElement fromXmlElement(Element element) {
        String tagName = element.tagName();
        Attributes attributes = element.attributes();
        return new ColorThemeElement(
                tagName,
                attributes.get("color"),
                fontTypeFromXmlElementAttr(attributes, IntellijIdeaColorScheme.FontBasicType.BOLD),
                fontTypeFromXmlElementAttr(attributes, IntellijIdeaColorScheme.FontBasicType.ITALIC),
                fontTypeFromXmlElementAttr(attributes, IntellijIdeaColorScheme.FontEffectType.UNDERLINE),
                fontTypeFromXmlElementAttr(attributes, IntellijIdeaColorScheme.FontEffectType.STRIKETHROUGH)
        );
    }

    public String toString() {
        List<String> fontProperties = new ArrayList<>(4);
        if (isBold())
            fontProperties.add("bold=\"true\"");
        if (isItalic())
            fontProperties.add("italic=\"true\"");
        if (isUnderline())
            fontProperties.add("underline=\"true\"");
        if (isStrikethrough())
            fontProperties.add("strikethrough=\"true\"");

        if (!fontProperties.isEmpty())
            fontProperties.add("");

        return String.format("<%s color=\"%s\" %s/>", name, colorValue, String.join(" ", fontProperties));
    }

    public String getColorValue() {
        return colorValue;
    }

    public boolean isBoldSetFalse() {
        return bold != null && !bold;
    }

    public Boolean isBold() {
        return bold != null && bold;
    }

    public boolean isItalicSetFalse() {
        return italic != null && !italic;
    }

    public Boolean isItalic() {
        return italic != null && italic;
    }

    public Boolean isUnderline() {
        return underline != null && underline;
    }

    public Boolean isStrikethrough() {
        return strikethrough != null && strikethrough;
    }
}
