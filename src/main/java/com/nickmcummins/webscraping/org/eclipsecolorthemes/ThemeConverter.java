package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.com.jetbrains.AttributeOption;
import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;

import java.util.*;

import static java.util.Map.entry;

public class ThemeConverter {
    private static final Map<String, List<String>> ECLIPSE_TO_IDEA_OPTIONS;
    static {
        ECLIPSE_TO_IDEA_OPTIONS = Map.of(
                "background", List.of("CONSOLE_BACKGROUND_KEY", "GUTTER_BACKGROUND", "RIGHT_MARGIN_COLOR", "TEARLINE_COLOR"),
                "selectionForeground", List.of("SELECTION_FOREGROUND"), "selectionBackground", List.of("SELECTION_BACKGROUND"),
                "currentLine", List.of("CARET_ROW_COLOR"),
                "lineNumber", List.of("CARET_COLOR", "LINE_NUMBERS_COLOR"));
    }
    private static final Map<String, String> ECLIPSE_TO_IDEA_ATTRIBUTES;
    static {
        ECLIPSE_TO_IDEA_ATTRIBUTES = Map.ofEntries(
                entry("occurrenceIndication", "IDENTIFIER_UNDER_CARET_ATTRIBUTES"),
                entry("writeOccurrenceIndication", "WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES"),
                entry("singleLineComment", "DEFAULT_LINE_COMMENT"),
                entry("multiLineComment", "DEFAULT_BLOCK_COMMENT"),
                entry("commentTaskTag", "TODO_DEFAULT_ATTRIBUTES"),
                entry("javadoc", "DEFAULT_DOC_COMMENT"),
                entry("javadocLink", "DEFAULT_DOC_COMMENT_TAG"),
                entry("javadocTag", "DEFAULT_DOC_MARKUP"),
                entry("javadocKeyword", "DEFAULT_DOC_COMMENT_TAG"),
                entry("class", "DEFAULT_CLASS_NAME"),
                entry("interface", "DEFAULT_INTERFACE_NAME"),
                entry("method", "DEFAULT_FUNCTION_CALL"),
                entry("methodDeclaration", "DEFAULT_FUNCTION_DECLARATION"),
                entry("bracket", "DEFAULT_BRACKETS"),
                entry("number", "DEFAULT_NUMBER"),
                entry("string", "DEFAULT_VALID_STRING_ESCAPE"),
                entry("operator", "DEFAULT_DOT"),
                entry("keyword", "DEFAULT_KEYWORD"),
                entry("annotation", "DEFAULT_METADATA"),
                entry("staticMethod", "DEFAULT_STATIC_METHOD"),
                entry("localVariableDeclaration", "DEFAULT_LOCAL_VARIABLE"),
                entry("field", "DEFAULT_INSTANCE_FIELD"),
                entry("staticFinalField", "STATIC_FINAL_FIELD_ATTRIBUTES"),
                entry("enum", "ENUM_NAME_ATTRIBUTES"),
                entry("inheritedMethod", "INHERITED_METHOD_ATTRIBUTES"),
                entry("abstractMethod", "ABSTRACT_METHOD_ATTRIBUTES"),
                entry("typeParameter", "TYPE_PARAMETER_NAME_ATTRIBUTES"),
                entry("constant", "DEFAULT_CONSTANT")
        );
    }

    public static void main(String[] args) {
        Map<String, String> iclsColorOptions = new HashMap<>();
        List<AttributeOption> iclsAttributeOptions = new ArrayList<>();

        EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromXml(args[0]);
        for (Map.Entry<String, ColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            String eclipseFieldName = colorThemeElement.getKey();
            ColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<String> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (String iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, eclipseColor.getColorValue());
            } else if (ECLIPSE_TO_IDEA_ATTRIBUTES.containsKey(eclipseFieldName)) {
                String iclsAttributeOptionName = ECLIPSE_TO_IDEA_ATTRIBUTES.get(eclipseFieldName);
                Map<String, String> attributeOptions = new HashMap<>();
                attributeOptions.put("FOREGROUND", eclipseColor.getColorValue());
                if (eclipseColor.isBold())
                    attributeOptions.put("FONT_TYPE", "1");
                if (eclipseColor.isItalic())
                    attributeOptions.put("FONT_TYPE", "2");
                if (eclipseColor.isStrikethrough())
                    attributeOptions.put("EFFECT_TYPE", "3");
                iclsAttributeOptions.add(new AttributeOption(iclsAttributeOptionName, attributeOptions));
            } else
                System.out.println(String.format("Skipping unmapped %s in Eclipse XML.", eclipseFieldName));
        }
        IntellijIdeaColorScheme icls = new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                iclsAttributeOptions);
        icls.writeToFile();
        System.out.println(icls);
    }
}