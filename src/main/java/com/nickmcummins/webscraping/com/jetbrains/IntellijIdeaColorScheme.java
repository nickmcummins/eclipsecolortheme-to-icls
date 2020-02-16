package com.nickmcummins.webscraping.com.jetbrains;

import com.nickmcummins.webscraping.ThemeFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntellijIdeaColorScheme implements ThemeFile {
    private static final String EXTENSION = "icls";
    private final Map<String, String> metaInfo;
    private final Map<String, String> colorOptions;
    private final List<AttributeOption> attributeOptions;

    public IntellijIdeaColorScheme(String created, String name, Map<String, String> colorOptions, List<AttributeOption> attributeOptions) {
        this.metaInfo = Map.of(
                "created", created != null ? created : new Date(System.currentTimeMillis()).toString(),
                "modofied", new Date(System.currentTimeMillis()).toString(),
                "originalScheme", name
        );
        this.colorOptions = colorOptions;
        this.attributeOptions = attributeOptions;
    }

    public String getName() {
        return metaInfo.get("originalScheme");
    }

    public String getExtension() {
        return EXTENSION;
    }

    public String toString() {
        String name = metaInfo.get("originalScheme");
        String created = metaInfo.get("created");
        String modified = metaInfo.get("modified");

        String colorsOptions = colorOptions.entrySet().stream()
                .map(colorOption -> String.format("<option name=\"%s\" value=\"%s\" />", colorOption.getKey(), colorOption.getValue()))
                .collect(Collectors.joining("\n\t\t"));
        String attributesOptions = attributeOptions.stream()
                .map(AttributeOption::toString)
                .collect(Collectors.joining("\n"));

        return String.format("""
                <scheme name="%s" version="142">
                    <metaInfo>
                        <property name="created">%s</property>
                        <property name="modified">%s</property>
                        <property name="originalScheme">%s</property>
                    </metaInfo>
                    <colors>
                        %s
                    </colors>
                    <attributes>
                %s
                    </attributes>
                </scheme>""", name, created, modified, name, colorsOptions, attributesOptions
        );
    }
}
