package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;

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
        List<String> valueStrings = new ArrayList<>(values.values());
        String formattedValueOptions;
        if (valueStrings.size() == 1 && valueStrings.get(0).equals("null"))
            formattedValueOptions = "<value />";
        else {
            formattedValueOptions = values.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> String.format("                <option name=\"%s\" value=\"%s\"/>", entry.getKey(), formatHexValue(entry.getValue())))
                    .collect(Collectors.joining("\n"));
        }
        return String.format("        <option name=\"%s\">\n            <value>\n%s\n            </value>\n        </option>", name, formattedValueOptions);
    }
}