package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.Comparator;
import java.util.List;
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

    public String getName() {
        return name;
    }

    public String toString() {
        List<String> valueStrings = values.values().stream().collect(Collectors.toList());
        String valueOptions;
        if (valueStrings.size() == 1 && valueStrings.get(0).equals("null"))
            valueOptions = String.format("<value />");
        else
            valueOptions = String.format("<value>\n%s\n\t\t\t</value>", values.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> String.format("\t\t\t\t<option name=\"%s\" value=\"%s\"/>", entry.getKey(), formatHexValue(entry.getValue())))
                    .collect(Collectors.joining("\n")));
        return String.format("""
                \t\t<option name="%s">
                \t\t\t%s
                \t\t</option>""", name, valueOptions);
    }
}