package com.nickmcummins.webscraping.com.jetbrains;

import com.nickmcummins.webscraping.ColorTheme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.Util.formatDate;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.modified;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.originalScheme;
import static com.nickmcummins.webscraping.com.jetbrains.IclsOptionProperty.*;

public class IntellijIdeaColorScheme implements ColorTheme {
    public enum MetaInfoProperty {
        created,
        ide,
        ideVersion,
        modified,
        originalScheme
    }

    public enum ColorOption {
        CARET_COLOR,
        CARET_ROW_COLOR,
        CONSOLE_BACKGROUND_KEY,
        GUTTER_BACKGROUND,
        INDENT_GUIDE,
        LINE_NUMBERS_COLOR,
        RIGHT_MARGIN_COLOR,
        SELECTION_BACKGROUND,
        SELECTION_FOREGROUND,
        SOFT_WRAP_SIGN_COLOR,
        TEARLINE_COLOR,
        WHITESPACES
    }

    private static final String EXTENSION = "icls";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    static final List<IclsOptionProperty> ATTRIBUTE_OPTION_VALUE_ORDER = List.of(FOREGROUND, FONT_TYPE, BACKGROUND,
            EFFECT_COLOR, ERROR_STRIPE_COLOR, EFFECT_TYPE);

    private final Map<MetaInfoProperty, String> metaInfo;
    private final Map<ColorOption, String> colorOptions;
    private final Collection<IclsAttributeOption> attributeOptions;

    private IntellijIdeaColorScheme(Map<MetaInfoProperty, String> metaInfo, Map<ColorOption, String> colorOptions, Collection<IclsAttributeOption> attributeOptions) {
        this.metaInfo = metaInfo;
        this.colorOptions = colorOptions;
        this.attributeOptions = attributeOptions;
    }

    public IntellijIdeaColorScheme(LocalDateTime created, String name, Map<ColorOption, String> colorOptions, Collection<IclsAttributeOption> attributeOptions) {
        this(new HashMap<>(Map.of(
                MetaInfoProperty.created, formatDate(DATE_FORMAT, created),
                modified, DATE_FORMAT.format(LocalDateTime.now()),
                originalScheme, name
                )),
                colorOptions,
                attributeOptions);
    }

    public void updateMetaInfo(MetaInfoProperty property, String value) {
        metaInfo.put(property, value);
    }

    public String getName() {
        return metaInfo.get(originalScheme);
    }

    public String getExtension() {
        return EXTENSION;
    }

    public String toString() {
        String name = metaInfo.get(originalScheme);

        String metaInfoProperties = metaInfo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(property -> String.format("<property name=\"%s\">%s</property>", property.getKey(), property.getValue()))
                .collect(Collectors.joining("\n        "));
        String colorsOptions = colorOptions.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(colorOption -> String.format("<option name=\"%s\" value=\"%s\"/>", colorOption.getKey(), colorOption.getValue()))
                .collect(Collectors.joining("\n        "));
        String attributesOptions = attributeOptions.stream()
                .sorted(Comparator.comparing(attributeOption -> attributeOption.getName().toString()))
                .map(IclsAttributeOption::toString)
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
