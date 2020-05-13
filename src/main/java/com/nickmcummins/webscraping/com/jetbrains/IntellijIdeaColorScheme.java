package com.nickmcummins.webscraping.com.jetbrains;

import com.nickmcummins.webscraping.ColorTheme;
import org.jsoup.internal.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntellijIdeaColorScheme implements ColorTheme {
    private static final String EXTENSION = "icls";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final List<String> ATTRIBUTE_OPTION_VALUE_ORDER = List.of("FOREGROUND", "FONT_TYPE", "BACKGROUND",
            "EFFECT_COLOR", "ERROR_STRIPE_COLOR", "EFFECT_TYPE");
    private final Map<String, String> metaInfo;
    private final Map<String, String> colorOptions;
    private final List<AttributeOption> attributeOptions;

    public IntellijIdeaColorScheme(Map<String, String> metaInfo, Map<String, String> colorOptions, List<AttributeOption> attributeOptions) {
        this.metaInfo = metaInfo;
        this.colorOptions = colorOptions;
        this.attributeOptions = attributeOptions;
    }

    public IntellijIdeaColorScheme(String created, String name, Map<String, String> colorOptions, List<AttributeOption> attributeOptions) {
        this(Map.of(
                "created", DATE_FORMAT.format(StringUtil.isBlank(created) ? System.currentTimeMillis() : created),
                "modified", DATE_FORMAT.format(System.currentTimeMillis()),
                "originalScheme", name)
                        .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                colorOptions,
                attributeOptions);
    }

    public void updateMetaInfo(String propertyName, String value) {
        metaInfo.put(propertyName, value);
    }

    public String getName() {
        return metaInfo.get("originalScheme");
    }

    public String getExtension() {
        return EXTENSION;
    }

    public String toString() {
        String name = metaInfo.get("originalScheme");

        String metaInfoProperties = metaInfo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(property -> String.format("<property name=\"%s\">%s</property>", property.getKey(), property.getValue()))
                .collect(Collectors.joining("\n        "));
        String colorsOptions = colorOptions.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(colorOption -> String.format("<option name=\"%s\" value=\"%s\" />", colorOption.getKey(), colorOption.getValue()))
                .collect(Collectors.joining("\n        "));
        String attributesOptions = attributeOptions.stream()
                .sorted(Comparator.comparing(AttributeOption::getName))
                .map(AttributeOption::toString)
                .collect(Collectors.joining("\n"));

        return String.format("""
                <scheme name="%s" version="142">
                    <metaInfo>
                        %s
                    </metaInfo>
                    <colors>
                        %s
                    </colors>
                    <attributes>
                %s
                    </attributes>
                </scheme>""", name, metaInfoProperties, colorsOptions, attributesOptions
        );
    }
}
