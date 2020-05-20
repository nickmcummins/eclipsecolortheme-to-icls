package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ATTRIBUTE_OPTION_VALUE_ORDER;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.AttributeOption;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.OptionProperty;

public class AttributeOptionValues {
    private final AttributeOption attributeOption;
    private Map<OptionProperty, String> values;

    public AttributeOptionValues(AttributeOption attributeOption, Map<OptionProperty, String> values) {
        this.attributeOption = attributeOption;
        this.values = values;
    }

    public static AttributeOptionValues fromXml(Element attributeOption) {
        return new AttributeOptionValues(
                AttributeOption.valueOf(attributeOption.attr("name")),
                attributeOption.select("value").select("option").stream()
                        .collect(Collectors.toMap(option -> OptionProperty.valueOf(option.attr("name")), option -> option.attr("value"))));
    }

    public AttributeOptionValues withMappedValues(Map<String, String> valueMappings) {
        this.values = this.values.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, value -> valueMappings.getOrDefault(value.getValue(), value.getValue())));
        return this;
    }

    public AttributeOption getAttributeOption() {
        return attributeOption;
    }

    public String toString() {
        List<String> valueStrings = new ArrayList<>(values.values());
        String formattedValueOptions;
        if (valueStrings.size() == 1 && valueStrings.get(0).equals("null"))
            formattedValueOptions = "<value />";
        else {
            formattedValueOptions = values.entrySet().stream()
                    .sorted(Comparator.comparing(option -> ATTRIBUTE_OPTION_VALUE_ORDER.indexOf(option.getKey())))
                    .map(entry -> String.format("                <option name=\"%s\" value=\"%s\"/>", entry.getKey(), formatHexValue(entry.getValue())))
                    .collect(Collectors.joining("\n"));
        }
        return String.format("        <option name=\"%s\">\n            <value>\n%s\n            </value>\n        </option>", attributeOption, formattedValueOptions);
    }
}