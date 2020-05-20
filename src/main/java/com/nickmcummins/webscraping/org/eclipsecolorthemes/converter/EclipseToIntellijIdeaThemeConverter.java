package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.AttributeOptionValues;
import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.SchemeType;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.ColorThemeElement;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

import java.util.*;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.AttributeOption.*;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ColorOption.*;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.OptionProperty.*;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.SettingField.*;
import static java.util.Map.entry;

public class EclipseToIntellijIdeaThemeConverter implements ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> {
    public static final Map<EclipseColorTheme.SettingField, List<IntellijIdeaColorScheme.ColorOption>> ECLIPSE_TO_IDEA_OPTIONS = Map.of(
            background, List.of(CONSOLE_BACKGROUND_KEY, GUTTER_BACKGROUND),
            selectionForeground, List.of(SELECTION_FOREGROUND),
            selectionBackground, List.of(SELECTION_BACKGROUND),
            currentLine, List.of(CARET_ROW_COLOR),
            lineNumber, List.of(CARET_COLOR, LINE_NUMBERS_COLOR, RIGHT_MARGIN_COLOR, TEARLINE_COLOR)
    );
    private static final Map<SchemeType, Map<IntellijIdeaColorScheme.ColorOption, String>> ICLS_COLOR_OPTION_DEFAULTS = Map.of(
            LIGHT, Map.of(
                    INDENT_GUIDE, "a8a8a8",
                    SOFT_WRAP_SIGN_COLOR, "a8a8a8",
                    WHITESPACES, "a8a8a8")
    );
    public static final Map<EclipseColorTheme.SettingField, List<IntellijIdeaColorScheme.AttributeOption>> ECLIPSE_TO_IDEA_ATTRIBUTES = Map.ofEntries(
            entry(commentTaskTag, List.of(TODO_DEFAULT_ATTRIBUTES)),
            entry(javadoc, List.of(DEFAULT_DOC_COMMENT)),
            entry(javadocTag, List.of(DEFAULT_DOC_MARKUP)),
            entry(javadocKeyword, List.of(DEFAULT_DOC_COMMENT_TAG)),
            entry(classColor, List.of(DEFAULT_CLASS_NAME)),
            entry(interfaceColor, List.of(DEFAULT_INTERFACE_NAME)),
            entry(method, List.of(DEFAULT_FUNCTION_CALL)),
            entry(methodDeclaration, List.of(DEFAULT_FUNCTION_DECLARATION)),
            entry(bracket, List.of(DEFAULT_BRACES, DEFAULT_BRACKETS)),
            entry(number, List.of(DEFAULT_NUMBER)),
            entry(operator, List.of(DEFAULT_DOT, DEFAULT_SEMICOLON, DEFAULT_OPERATION_SIGN, DEFAULT_COMMA)),
            entry(annotation, List.of(DEFAULT_METADATA)),
            entry(staticMethod, List.of(DEFAULT_STATIC_METHOD)),
            entry(field, List.of(DEFAULT_INSTANCE_FIELD)),
            entry(staticFinalField, List.of(STATIC_FINAL_FIELD_ATTRIBUTES)),
            entry(enumColor, List.of(ENUM_NAME_ATTRIBUTES)),
            entry(inheritedMethod, List.of(INHERITED_METHOD_ATTRIBUTES)),
            entry(abstractMethod, List.of(ABSTRACT_METHOD_ATTRIBUTES)),
            entry(typeParameter, List.of(TYPE_PARAMETER_NAME_ATTRIBUTES))
    );
    public static final Map<SchemeType, List<AttributeOptionValues>> ICLS_CONSOLE_DEFAULTS = Map.of(
            LIGHT, List.of(
                    new AttributeOptionValues(BAD_CHARACTER, Map.of(BACKGROUND, "ffcccc")),
                    new AttributeOptionValues(BREAKPOINT_ATTRIBUTES, Map.of(BACKGROUND, "faeae6", ERROR_STRIPE_COLOR, "ffc8c8")),
                    new AttributeOptionValues(CONSOLE_BLACK_OUTPUT, Map.of(FOREGROUND, "0")),
                    new AttributeOptionValues(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(FOREGROUND, "5c5cff")),
                    new AttributeOptionValues(CONSOLE_BLUE_OUTPUT, Map.of(FOREGROUND, "ee")),
                    new AttributeOptionValues(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ffff")),
                    new AttributeOptionValues(CONSOLE_CYAN_OUTPUT, Map.of(FOREGROUND, "cccc")),
                    new AttributeOptionValues(CONSOLE_DARKGRAY_OUTPUT, Map.of(FOREGROUND, "555555")),
                    new AttributeOptionValues(CONSOLE_ERROR_OUTPUT, Map.of(FOREGROUND, "7f0000")),
                    new AttributeOptionValues(CONSOLE_GRAY_OUTPUT, Map.of(FOREGROUND, "aaaaaa")),
                    new AttributeOptionValues(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff00")),
                    new AttributeOptionValues(CONSOLE_GREEN_OUTPUT, Map.of(FOREGROUND, "cd00")),
                    new AttributeOptionValues(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff00ff")),
                    new AttributeOptionValues(CONSOLE_MAGENTA_OUTPUT, Map.of(FOREGROUND, "cd00cd")),
                    new AttributeOptionValues(CONSOLE_NORMAL_OUTPUT, Map.of(FOREGROUND, "0")),
                    new AttributeOptionValues(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff0000")),
                    new AttributeOptionValues(CONSOLE_RED_OUTPUT, Map.of(FOREGROUND, "cd0000")),
                    new AttributeOptionValues(CONSOLE_SYSTEM_OUTPUT, Map.of(FOREGROUND, "7f")),
                    new AttributeOptionValues(CONSOLE_USER_INPUT, Map.of(FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new AttributeOptionValues(CONSOLE_WHITE_OUTPUT, Map.of(FOREGROUND, "ffffff")),
                    new AttributeOptionValues(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(FOREGROUND, "eaea00")),
                    new AttributeOptionValues(CONSOLE_YELLOW_OUTPUT, Map.of(FOREGROUND, "c4a000")),
                    new AttributeOptionValues(CTRL_CLICKABLE, Map.of(FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(DEBUGGER_INLINED_VALUES, Map.of(FOREGROUND, "868686", FONT_TYPE, "2")),
                    new AttributeOptionValues(DEBUGGER_INLINED_VALUES_EXECUTION_LINE, Map.of(FOREGROUND, "a9b7d6", FONT_TYPE, "2")),
                    new AttributeOptionValues(DEBUGGER_INLINED_VALUES_MODIFIED, Map.of(FOREGROUND, "ca8021", FONT_TYPE, "2")),
                    new AttributeOptionValues(DEFAULT_BLOCK_COMMENT, Map.of(FOREGROUND, multiLineComment.toString())),
                    new AttributeOptionValues(DEFAULT_CONSTANT, Map.of(FOREGROUND, constant.toString())),
                    new AttributeOptionValues(DEFAULT_IDENTIFIER, Map.of(FOREGROUND, foreground.toString(), BACKGROUND, background.toString())),
                    new AttributeOptionValues(DEFAULT_ENTITY, Map.of(FOREGROUND, localVariableDeclaration.toString(), FONT_TYPE, "1")),
                    new AttributeOptionValues(DEFAULT_INVALID_STRING_ESCAPE, Map.of(FOREGROUND, "8000", BACKGROUND, "ffcccc")),
                    new AttributeOptionValues(DEFAULT_KEYWORD, Map.of(FOREGROUND, keyword.toString(), FONT_TYPE, "1")),
                    new AttributeOptionValues(DEFAULT_LINE_COMMENT, Map.of(FOREGROUND, singleLineComment.toString())),
                    new AttributeOptionValues(DEFAULT_LOCAL_VARIABLE, Map.of(FOREGROUND, localVariable.toString())),
                    new AttributeOptionValues(DEFAULT_PARAMETER, Map.of(FOREGROUND, parameterVariable.toString())),
                    new AttributeOptionValues(DEFAULT_PARENTHS, Map.of(FOREGROUND, bracket.toString())),
                    new AttributeOptionValues(DEFAULT_STATIC_FIELD, Map.of(FOREGROUND, staticField.toString())),
                    new AttributeOptionValues(DEFAULT_STRING, Map.of(FOREGROUND, stringColor.name())),
                    new AttributeOptionValues(DEFAULT_TAG, Map.of(FOREGROUND, typeArgument.name())),
                    new AttributeOptionValues(DEFAULT_VALID_STRING_ESCAPE, Map.of(FOREGROUND, stringColor.name(), FONT_TYPE, "1")),
                    new AttributeOptionValues(DEPRECATED_ATTRIBUTES, Map.of(EFFECT_COLOR, "404040", EFFECT_TYPE, "3")),
                    new AttributeOptionValues(DIFF_ABSENT, Map.of(BACKGROUND, "f0f0f0")),
                    new AttributeOptionValues(DIFF_CONFLICT, Map.of(BACKGROUND, "ffd5cc", ERROR_STRIPE_COLOR, "ffc8bd")),
                    new AttributeOptionValues(DIFF_DELETED, Map.of(BACKGROUND, "d6d6d6", ERROR_STRIPE_COLOR, "c8c8c8")),
                    new AttributeOptionValues(DIFF_INSERTED, Map.of(BACKGROUND, "bee6be", ERROR_STRIPE_COLOR, "aadeaa")),
                    new AttributeOptionValues(DIFF_MODIFIED, Map.of(BACKGROUND, "cad9fa", ERROR_STRIPE_COLOR, "b8cbf5")),
                    new AttributeOptionValues(DUPLICATE_FROM_SERVER, Map.of(BACKGROUND, "f5f7f0")),
                    new AttributeOptionValues(ERRORS_ATTRIBUTES, Map.of(EFFECT_COLOR, "ff0000", ERROR_STRIPE_COLOR, "cf5b56", EFFECT_TYPE, "2")),
                    new AttributeOptionValues(EXECUTIONPOINT_ATTRIBUTES, Map.of(FOREGROUND, "ffffff", BACKGROUND, "2154a6", ERROR_STRIPE_COLOR, "2154a6")),
                    new AttributeOptionValues(FOLLOWED_HYPERLINK_ATTRIBUTES, Map.of(FOREGROUND, "ff", BACKGROUND, "e9e9e9", EFFECT_COLOR, "ff", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(GENERIC_SERVER_ERROR_OR_WARNING, Map.of(EFFECT_COLOR, "f49810", ERROR_STRIPE_COLOR, "e69317", EFFECT_TYPE, "2")),
                    new AttributeOptionValues(HTML_ATTRIBUTE_NAME, Map.of(FOREGROUND, field.name())),
                    new AttributeOptionValues(HTML_TAG_NAME, Map.of(FOREGROUND, keyword.name())),
                    new AttributeOptionValues(HYPERLINK_ATTRIBUTES, Map.of(FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(BACKGROUND, occurrenceIndication.toString())),
                    new AttributeOptionValues(INFORMATION_ATTRIBUTES, Map.of()),
                    new AttributeOptionValues(INFO_ATTRIBUTES, Map.of(EFFECT_COLOR, "cccccc", ERROR_STRIPE_COLOR, "d9cfad", EFFECT_TYPE, "2")),
                    new AttributeOptionValues(LINE_FULL_COVERAGE, Map.of(FOREGROUND, "ccffcc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LINE_NONE_COVERAGE, Map.of(FOREGROUND, "ffcccc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LINE_PARTIAL_COVERAGE, Map.of(FOREGROUND, "ffffcc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LOG_ERROR_OUTPUT, Map.of(FOREGROUND, "cd0000")),
                    new AttributeOptionValues(LOG_EXPIRED_ENTRY, Map.of(FOREGROUND, foreground.toString())),
                    new AttributeOptionValues(LOG_WARNING_OUTPUT, Map.of(FOREGROUND, "a66f00")),
                    new AttributeOptionValues(MATCHED_BRACE_ATTRIBUTES, Map.of(EFFECT_COLOR, "a8a8a8")),
                    new AttributeOptionValues(NOT_TOP_FRAME_ATTRIBUTES, Map.of(BACKGROUND, "c0d0f0")),
                    new AttributeOptionValues(NOT_USED_ELEMENT_ATTRIBUTES, Map.of(FOREGROUND, "808080")),
                    new AttributeOptionValues(SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, "dddddd")),
                    new AttributeOptionValues(TEXT, Map.of(FOREGROUND, foreground.toString(), BACKGROUND, background.toString())),
                    new AttributeOptionValues(TEXT_SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, "dddddd")),
                    new AttributeOptionValues(WARNING_ATTRIBUTES, Map.of(BACKGROUND, "f6ebbc", ERROR_STRIPE_COLOR, "ebc700", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(BACKGROUND, "dddddd")),
                    new AttributeOptionValues(WRONG_REFERENCES_ATTRIBUTES, Map.of(FOREGROUND, "ff0000")),
                    new AttributeOptionValues(XML_ATTRIBUTE_NAME, Map.of(FOREGROUND, field.name())),
                    new AttributeOptionValues(XML_TAG_NAME, Map.of(FOREGROUND, keyword.name()))),

            DARK, List.of(
                    new AttributeOptionValues(BAD_CHARACTER, Map.of(BACKGROUND, "ff0000")),
                    new AttributeOptionValues(BREAKPOINT_ATTRIBUTES, Map.of(BACKGROUND, "3a2323", ERROR_STRIPE_COLOR, "664233")),
                    new AttributeOptionValues(CONSOLE_BLACK_OUTPUT, Map.of(FOREGROUND, "ffffff")),
                    new AttributeOptionValues(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(FOREGROUND, "7eaef1")),
                    new AttributeOptionValues(CONSOLE_BLUE_OUTPUT, Map.of(FOREGROUND, "5394ec")),
                    new AttributeOptionValues(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "6cdada")),
                    new AttributeOptionValues(CONSOLE_CYAN_OUTPUT, Map.of(FOREGROUND, "299999")),
                    new AttributeOptionValues(CONSOLE_DARKGRAY_OUTPUT, Map.of(FOREGROUND, "555555")),
                    new AttributeOptionValues(CONSOLE_ERROR_OUTPUT, Map.of(FOREGROUND, "ff6b68")),
                    new AttributeOptionValues(CONSOLE_GRAY_OUTPUT, Map.of(FOREGROUND, "999999")),
                    new AttributeOptionValues(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "a8c023")),
                    new AttributeOptionValues(CONSOLE_GREEN_OUTPUT, Map.of(FOREGROUND, "a8c023")),
                    new AttributeOptionValues(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff99ff")),
                    new AttributeOptionValues(CONSOLE_MAGENTA_OUTPUT, Map.of(FOREGROUND, "ae8abe")),
                    new AttributeOptionValues(CONSOLE_NORMAL_OUTPUT, Map.of(FOREGROUND, "bbbbbb")),
                    new AttributeOptionValues(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff8785")),
                    new AttributeOptionValues(CONSOLE_RED_OUTPUT, Map.of(FOREGROUND, "ff6b68")),
                    new AttributeOptionValues(CONSOLE_SYSTEM_OUTPUT, Map.of(FOREGROUND, "bbbbbb")),
                    new AttributeOptionValues(CONSOLE_USER_INPUT, Map.of(FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new AttributeOptionValues(CONSOLE_WHITE_OUTPUT, Map.of(FOREGROUND, "1f1f1f")),
                    new AttributeOptionValues(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ffff00")),
                    new AttributeOptionValues(CONSOLE_YELLOW_OUTPUT, Map.of(FOREGROUND, "d6bf55")),
                    new AttributeOptionValues(DEBUGGER_INLINED_VALUES, Map.of(FOREGROUND, "66d75", FONT_TYPE, "2")),
                    new AttributeOptionValues(DIFF_ABSENT, Map.of(FOREGROUND, "null")),
                    new AttributeOptionValues(DIFF_CONFLICT, Map.of(BACKGROUND, "45302b")),
                    new AttributeOptionValues(DIFF_DELETED, Map.of(BACKGROUND, "484a4a")),
                    new AttributeOptionValues(DIFF_INSERTED, Map.of(BACKGROUND, "294436")),
                    new AttributeOptionValues(DIFF_MODIFIED, Map.of(BACKGROUND, "385570")))
    );

    @Override
    public IntellijIdeaColorScheme convert(EclipseColorTheme eclipseColorTheme) {
        Map<IntellijIdeaColorScheme.ColorOption, String> iclsColorOptions = new HashMap<>();
        List<AttributeOptionValues> iclsAttributeOptions = new ArrayList<>();

        for (Map.Entry<EclipseColorTheme.SettingField, ColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            EclipseColorTheme.SettingField eclipseFieldName = colorThemeElement.getKey();
            ColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<IntellijIdeaColorScheme.ColorOption> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (IntellijIdeaColorScheme.ColorOption iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            } else if (ECLIPSE_TO_IDEA_ATTRIBUTES.containsKey(eclipseFieldName)) {
                for (IntellijIdeaColorScheme.AttributeOption iclsAttributeOptionName : ECLIPSE_TO_IDEA_ATTRIBUTES.get(eclipseFieldName)) {
                    Map<IntellijIdeaColorScheme.OptionProperty, String> attributeOptions = new HashMap<>();
                    attributeOptions.put(FOREGROUND, eclipseColor.getColorValue());
                    if (eclipseColor.isBold())
                        attributeOptions.put(FONT_TYPE, "1");
                    if (eclipseColor.isItalic())
                        attributeOptions.put(FONT_TYPE, "2");
                    if (eclipseColor.isStrikethrough())
                        attributeOptions.put(EFFECT_TYPE, "3");
                    iclsAttributeOptions.add(new AttributeOptionValues(iclsAttributeOptionName, attributeOptions));
                }
            } else
                System.out.println(String.format("Skipping unmapped %s in Eclipse XML.", eclipseFieldName));
        }

        for (Map.Entry<IntellijIdeaColorScheme.ColorOption, String> colorOptionDefault : ICLS_COLOR_OPTION_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).entrySet()) {
            iclsColorOptions.put(colorOptionDefault.getKey(), colorOptionDefault.getValue());
        }

        Map<String, String> eclipseToIclsValueMappings = eclipseColorTheme.getSettingsByName().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> entry.getValue().getColorValue()));

        List<AttributeOptionValues> consoleAttributeOptions = ICLS_CONSOLE_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).stream()
                .map(consoleAttributeOption -> consoleAttributeOption.withMappedValues(eclipseToIclsValueMappings))
                .collect(Collectors.toList());

        iclsAttributeOptions.addAll(consoleAttributeOptions);

        return new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                iclsAttributeOptions);
    }
}