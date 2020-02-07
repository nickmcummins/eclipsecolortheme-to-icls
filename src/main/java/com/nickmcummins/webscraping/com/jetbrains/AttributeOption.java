package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public class AttributeOption
{
    private String name;
    private Map<String, String> values;

    public AttributeOption(Element attributeOption)
    {
        this.name = attributeOption.attr("name");
        this.values = attributeOption.select("value").select("option").stream()
                .collect(Collectors.toMap(option -> option.attr("name"), option -> option.attr("value")));
    }

    public String toString()
    {
        String valueOptions = values.entrySet().stream()
                .map(entry -> String.format("<option name=\"%s\" value=\"%s\" />", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
        return String.format("""
                <option name="%s">
                    <value>
                    %s
                    </value>
                </option>
                
                """, name, valueOptions);
    }
}
