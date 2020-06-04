package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.nickmcummins.webscraping.Util.camelToSnake;

public class EclipseColorThemeSettingElement {
    public final String name;
    // values
    private final String colorValue;
    private final Boolean bold;
    private final Boolean italic;
    private final Boolean underline;
    private final Boolean strikethrough;

    private EclipseColorThemeSettingElement(String name, String colorValue, Boolean bold, Boolean italic, Boolean underline, Boolean strikethrough) {
        this.name = name;
        this.colorValue = colorValue;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
    }

    private static Boolean fontTypeFromHtmlElementAttr(Element div, EclipseFontProperty fontType) {
        String elementClassSelector = String.format("a[class='%s-active']", fontType.name().toLowerCase());

        Elements aElements = div.select(elementClassSelector);
        if (!aElements.isEmpty()) {
            return aElements.get(0).attr("href").contains("false");
        }

        return null;
    }

    static EclipseColorThemeSettingElement fromHtmlPageDiv(Element div) {
        return new EclipseColorThemeSettingElement(
                div.select("div[class='setting']").get(0).text(),
                div.select("input").get(0).attr("value"),
                fontTypeFromHtmlElementAttr(div, EclipseFontProperty.BOLD),
                fontTypeFromHtmlElementAttr(div, EclipseFontProperty.ITALIC),
                fontTypeFromHtmlElementAttr(div, EclipseFontProperty.UNDERLINE),
                fontTypeFromHtmlElementAttr(div, EclipseFontProperty.STRIKETHROUGH)
        );
    }

    private static Boolean fontTypeFromXmlElementAttr(Attributes attributes, EclipseFontProperty fontType) {
        String attr = fontType.name().toLowerCase();
        if (attributes.hasKey(attr)) {
            return Boolean.parseBoolean(attributes.get(attr));
        }

        return null;
    }

    static EclipseColorThemeSettingElement fromXmlElement(Element element) {
        String tagName = element.tagName();
        Attributes attributes = element.attributes();
        return new EclipseColorThemeSettingElement(
                tagName,
                attributes.get("color"),
                fontTypeFromXmlElementAttr(attributes, EclipseFontProperty.BOLD),
                fontTypeFromXmlElementAttr(attributes, EclipseFontProperty.ITALIC),
                fontTypeFromXmlElementAttr(attributes, EclipseFontProperty.UNDERLINE),
                fontTypeFromXmlElementAttr(attributes, EclipseFontProperty.STRIKETHROUGH)
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

    public enum Name {
        SELECTION_BACKGROUND,
        TYPE_PARAMETER,
        METHOD_DECLARATION,
        MULTI_LINE_COMMENT,
        CONSTANT,
        STRING,
        SEARCH_RESULT_INDICATION,
        TYPE_ARGUMENT,
        SINGLE_LINE_COMMENT,
        FOREGROUND,
        INTERFACE,
        OPERATOR,
        STATIC_FIELD,
        JAVADOC_KEYWORD,
        JAVADOC,
        NUMBER,
        STATIC_FINAL_FIELD,
        LOCAL_VARIABLE,
        DELETION_INDICATION,
        BRACKET,
        DEPRECATED_MEMBER,
        KEYWORD,
        CLASS,
        INHERITED_METHOD,
        CURRENT_LINE,
        ANNOTATION,
        WRITE_OCCURRENCE_INDICATION,
        JAVADOC_LINK,
        SELECTION_FOREGROUND,
        METHOD,
        FIND_SCOPE,
        JAVADOC_TAG,
        PARAMETER_VARIABLE,
        ENUM,
        COMMENT_TASK_TAG,
        LOCAL_VARIABLE_DECLARATION,
        FIELD,
        BACKGROUND,
        OCCURRENCE_INDICATION,
        ABSTRACT_METHOD,
        LINE_NUMBER,
        STATIC_METHOD,
        FILTERED_SEARCH_RESULT_INDICATION,
        SOURCE_HOVER_BACKGROUND;


        public static Name fromString(String settingString) {
            return Name.valueOf(camelToSnake(settingString));
        }

        public static Name fromColorThemeElement(EclipseColorThemeSettingElement colorThemeElement) {
            return fromString(colorThemeElement.name);
        }

        public static Name fromXmlElement(Element xmlElement) {
            return fromString(xmlElement.tagName());
        }
    }
}
