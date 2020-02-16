package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.ThemeConverter.formatHexValue;

public class AttributeOption {
    private final String name;
    private final Map<String, String> values;

    public AttributeOption(String name, Map<String, String> values) {
        this.name = name;
        this.values = values;
    }

    public static AttributeOption fromXml(Element attributeOption) {
        return new AttributeOption(
                attributeOption.attr("name"),
                attributeOption.select("value").select("option").stream()
                        .collect(Collectors.toMap(option -> option.attr("name"), option -> option.attr("value"))));
    }

    public String toString() {
        String valueOptions = values.entrySet().stream()
                .map(entry -> String.format("<option name=\"%s\" value=\"%s\" />", entry.getKey(), formatHexValue(entry.getValue())))
                .collect(Collectors.joining("\n"));
        return String.format("""
                \t\t<option name="%s">
                \t\t\t<value>
                \t\t\t\t%s
                \t\t\t</value>
                \t\t</option>""", name, valueOptions);
    }
}