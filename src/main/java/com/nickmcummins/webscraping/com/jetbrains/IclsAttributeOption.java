package com.nickmcummins.webscraping.com.jetbrains;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.com.jetbrains.IclsColorScheme.ATTRIBUTE_OPTION_VALUE_ORDER;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class IclsAttributeOption {
    public interface Name {
        Map<String, String> getColorMapper();
    }

    private final IclsAttributeOption.Name name;
    private Map<IclsOptionProperty, String> values;

    public IclsAttributeOption(IclsAttributeOption.Name name, Map<IclsOptionProperty, String> values) {
        this.name = name;
        this.values = values;
    }

    public void addAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName, String value) {
        if (!(values instanceof HashMap))
            values = new HashMap<>(values);

        values.put(IclsOptionPropertyName, value);
    }

    public void removeAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName) {
        values.remove(IclsOptionPropertyName);
    }

    public IclsAttributeOption.Name getName() {
        return name;
    }

    public Map<IclsOptionProperty, String> getValues() {
        return values;
    }

    public String toString() {
        List<String> valueStrings = values.values().stream().filter(value -> !isBlank(value) && !value.equals("null")).collect(Collectors.toList());
        String valuesTag = valueStrings.isEmpty()
                ? "<value/>\n"
                : String.format("<value>\n%s\n            </value>\n", values.entrySet().stream().sorted(Comparator.comparing(option -> ATTRIBUTE_OPTION_VALUE_ORDER.indexOf(option.getKey()))).map(entry -> String.format("                <option name=\"%s\" value=\"%s\"/>", entry.getKey(), formatHexValue(entry.getValue()))).collect(Collectors.joining("\n")));
        return String.format("        <option name=\"%s\">\n            %s        </option>", name, valuesTag);
    }
}
