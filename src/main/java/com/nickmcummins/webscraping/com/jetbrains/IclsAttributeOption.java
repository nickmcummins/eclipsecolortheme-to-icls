package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.*;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ATTRIBUTE_OPTION_VALUE_ORDER;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.AttributeOptionName;

public class IclsAttributeOption {
    private final AttributeOptionName name;
    private Map<IclsOptionProperty, String> values;

    public IclsAttributeOption(AttributeOptionName name, Map<IclsOptionProperty, String> values) {
        this.name = name;
        this.values = values;
    }

    public static IclsAttributeOption fromXml(Element attributeOption) {
        return new IclsAttributeOption(
                AttributeOptionName.valueOf(attributeOption.attr("name")),
                attributeOption.select("value").select("option").stream()
                        .collect(Collectors.toMap(option -> IclsOptionProperty.valueOf(option.attr("name")), option -> option.attr("value"))));
    }

    public void addAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName, String value) {
        if (!(values instanceof HashMap))
            values = new HashMap<>(values);

        values.put(IclsOptionPropertyName, value);
    }

    public void removeAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName) {
        values.remove(IclsOptionPropertyName);
    }

    public AttributeOptionName getName() {
        return name;
    }

    public Map<IclsOptionProperty, String> getValues() {
        return values;
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
        String valuesTag = values.isEmpty() ? "<value/>\n" : String.format("<value>\n%s\n            </value>\n", formattedValueOptions);
        return String.format("        <option name=\"%s\">\n            %s        </option>", name, valuesTag);
    }
}