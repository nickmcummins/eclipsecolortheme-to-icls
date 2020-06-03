package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.*;
import com.nickmcummins.webscraping.SchemeType;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeElement;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

import java.util.*;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.AttributeOptionName.*;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ColorOption.*;
import static com.nickmcummins.webscraping.com.jetbrains.IclsOptionProperty.*;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.SettingField.*;
import static java.util.Map.entry;

public class EclipseToIntellijIdeaThemeConverter implements ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> {
    private static final Map<EclipseColorTheme.SettingField, List<IntellijIdeaColorScheme.ColorOption>> ECLIPSE_TO_IDEA_OPTIONS = Map.of(
            background, List.of(CONSOLE_BACKGROUND_KEY, GUTTER_BACKGROUND),
            selectionForeground, List.of(SELECTION_FOREGROUND),
            selectionBackground, List.of(SELECTION_BACKGROUND),
            currentLine, List.of(CARET_ROW_COLOR),
            lineNumber, List.of(CARET_COLOR, LINE_NUMBERS_COLOR, RIGHT_MARGIN_COLOR, TEARLINE_COLOR)
    );
    private static final List<IntellijIdeaColorScheme.ColorOption> ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_OPTIONS = List.of(
            INDENT_GUIDE, SOFT_WRAP_SIGN_COLOR, WHITESPACES
    );
    public static final Map<String, String> ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS = Map.of(
            "#000000", "808080",
            "#007400", "7db87c",
            "#333333", "999999",
            "#430400", "a18280",
            "#555555", "a8a8a8",
            "#525B5B", "a9adad",
            "#585858", "a7a7a7",
            "#8000FF", "c080ff",
            "#C0B6A8", "e0dbd4",
            "#FF5050", "ffa8a8"
    );
    private static final Map<IntellijIdeaColorScheme.AttributeOptionName, Map<IclsOptionProperty, EclipseColorTheme.SettingField>> ECLIPSE_TO_ICLS_ATTRIBUTES = Map.ofEntries(
            entry(ABSTRACT_METHOD_ATTRIBUTES, Map.of(FOREGROUND, abstractMethod)),
            entry(DEFAULT_BRACES, Map.of(FOREGROUND, bracket)),
            entry(DEFAULT_BRACKETS, Map.of(FOREGROUND, bracket)),
            entry(DEFAULT_BLOCK_COMMENT, Map.of(FOREGROUND, multiLineComment)),
            entry(DEFAULT_COMMA, Map.of(FOREGROUND, operator)),
            entry(DEFAULT_CONSTANT, Map.of(FOREGROUND, constant)),
            entry(DEFAULT_DOC_COMMENT, Map.of(FOREGROUND, javadoc)),
            entry(DEFAULT_DOC_MARKUP, Map.of(FOREGROUND, javadocTag)),
            entry(DEFAULT_DOC_COMMENT_TAG, Map.of(FOREGROUND, javadocKeyword)),
            entry(DEFAULT_DOT, Map.of(FOREGROUND, operator)),
            entry(DEFAULT_CLASS_NAME, Map.of(FOREGROUND, classColor)),
            entry(DEFAULT_ENTITY, Map.of(FOREGROUND, keyword)),
            entry(DEFAULT_FUNCTION_CALL, Map.of(FOREGROUND, method)),
            entry(DEFAULT_FUNCTION_DECLARATION, Map.of(FOREGROUND, methodDeclaration)),
            entry(DEFAULT_INTERFACE_NAME, Map.of(FOREGROUND, interfaceColor)),
            entry(DEFAULT_INSTANCE_FIELD, Map.of(FOREGROUND, field)),
            entry(DEFAULT_IDENTIFIER, Map.of(FOREGROUND, foreground)),
            entry(DEFAULT_KEYWORD, Map.of(FOREGROUND, keyword)),
            entry(DEFAULT_LINE_COMMENT, Map.of(FOREGROUND, singleLineComment)),
            entry(DEFAULT_LOCAL_VARIABLE, Map.of(FOREGROUND, localVariable)),
            entry(DEFAULT_PARAMETER, Map.of(FOREGROUND, parameterVariable)),
            entry(DEFAULT_METADATA, Map.of(FOREGROUND, annotation)),
            entry(DEFAULT_NUMBER, Map.of(FOREGROUND, number)),
            entry(DEFAULT_OPERATION_SIGN, Map.of(FOREGROUND, operator)),
            entry(DEFAULT_PARENTHS, Map.of(FOREGROUND, bracket)),
            entry(DEFAULT_SEMICOLON, Map.of(FOREGROUND, operator)),
            entry(DEFAULT_STATIC_FIELD, Map.of(FOREGROUND, staticField)),
            entry(DEFAULT_STATIC_METHOD, Map.of(FOREGROUND, staticMethod)),
            entry(DEFAULT_STRING, Map.of(FOREGROUND, stringColor)),
            entry(DEFAULT_TAG, Map.of(FOREGROUND, localVariable)),
            entry(DEFAULT_VALID_STRING_ESCAPE, Map.of(FOREGROUND, stringColor)),
            entry(ENUM_NAME_ATTRIBUTES, Map.of(FOREGROUND, enumColor)),
            entry(HTML_ATTRIBUTE_NAME, Map.of(FOREGROUND, field)),
            entry(HTML_TAG_NAME, Map.of(FOREGROUND, localVariableDeclaration)),
            entry(IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(BACKGROUND, occurrenceIndication)),
            entry(INHERITED_METHOD_ATTRIBUTES, Map.of(FOREGROUND, inheritedMethod)),
            entry(MATCHED_BRACE_ATTRIBUTES, Map.of(EFFECT_COLOR, foreground)),
            entry(SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, searchResultIndication)),
            entry(STATIC_FINAL_FIELD_ATTRIBUTES, Map.of(FOREGROUND, staticFinalField)),
            entry(TEXT, Map.of(FOREGROUND, foreground, BACKGROUND, background)),
            entry(TEXT_SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, filteredSearchResultIndication)),
            entry(TODO_DEFAULT_ATTRIBUTES, Map.of(FOREGROUND, commentTaskTag)),
            entry(TYPE_PARAMETER_NAME_ATTRIBUTES, Map.of(FOREGROUND, typeArgument)),
            entry(WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(BACKGROUND, writeOccurrenceIndication)),
            entry(XML_ATTRIBUTE_NAME, Map.of(FOREGROUND, field)),
            entry(XML_TAG_NAME, Map.of(FOREGROUND, localVariableDeclaration))
    );
    private static final Map<SchemeType, List<IclsAttributeOption>> ICLS_CONSOLE_DEFAULTS = Map.of(
            LIGHT, List.of(
                    new IclsAttributeOption(BAD_CHARACTER, Map.of(BACKGROUND, "ffcccc")),
                    new IclsAttributeOption(BREAKPOINT_ATTRIBUTES, Map.of(BACKGROUND, "faeae6", ERROR_STRIPE_COLOR, "ffc8c8")),
                    new IclsAttributeOption(CONSOLE_BLACK_OUTPUT, Map.of(FOREGROUND, "0")),
                    new IclsAttributeOption(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(FOREGROUND, "5c5cff")),
                    new IclsAttributeOption(CONSOLE_BLUE_OUTPUT, Map.of(FOREGROUND, "ee")),
                    new IclsAttributeOption(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ffff")),
                    new IclsAttributeOption(CONSOLE_CYAN_OUTPUT, Map.of(FOREGROUND, "cccc")),
                    new IclsAttributeOption(CONSOLE_DARKGRAY_OUTPUT, Map.of(FOREGROUND, "555555")),
                    new IclsAttributeOption(CONSOLE_ERROR_OUTPUT, Map.of(FOREGROUND, "7f0000")),
                    new IclsAttributeOption(CONSOLE_GRAY_OUTPUT, Map.of(FOREGROUND, "aaaaaa")),
                    new IclsAttributeOption(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff00")),
                    new IclsAttributeOption(CONSOLE_GREEN_OUTPUT, Map.of(FOREGROUND, "cd00")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff00ff")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_OUTPUT, Map.of(FOREGROUND, "cd00cd")),
                    new IclsAttributeOption(CONSOLE_NORMAL_OUTPUT, Map.of(FOREGROUND, "0")),
                    new IclsAttributeOption(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff0000")),
                    new IclsAttributeOption(CONSOLE_RED_OUTPUT, Map.of(FOREGROUND, "cd0000")),
                    new IclsAttributeOption(CONSOLE_SYSTEM_OUTPUT, Map.of(FOREGROUND, "7f")),
                    new IclsAttributeOption(CONSOLE_USER_INPUT, Map.of(FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new IclsAttributeOption(CONSOLE_WHITE_OUTPUT, Map.of(FOREGROUND, "ffffff")),
                    new IclsAttributeOption(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(FOREGROUND, "eaea00")),
                    new IclsAttributeOption(CONSOLE_YELLOW_OUTPUT, Map.of(FOREGROUND, "c4a000")),
                    new IclsAttributeOption(CTRL_CLICKABLE, Map.of(FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES, Map.of(FOREGROUND, "868686", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_EXECUTION_LINE, Map.of(FOREGROUND, "a9b7d6", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_MODIFIED, Map.of(FOREGROUND, "ca8021", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEFAULT_ENTITY, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_KEYWORD, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_INVALID_STRING_ESCAPE, Map.of(FOREGROUND, "8000", BACKGROUND, "ffcccc")),
                    new IclsAttributeOption(DEFAULT_VALID_STRING_ESCAPE, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEPRECATED_ATTRIBUTES, Map.of(EFFECT_COLOR, "404040", EFFECT_TYPE, IclsFontEffectType.STRIKETHROUGH.value)),
                    new IclsAttributeOption(DIFF_ABSENT, Map.of(BACKGROUND, "f0f0f0")),
                    new IclsAttributeOption(DIFF_CONFLICT, Map.of(BACKGROUND, "ffd5cc", ERROR_STRIPE_COLOR, "ffc8bd")),
                    new IclsAttributeOption(DIFF_DELETED, Map.of(BACKGROUND, "d6d6d6", ERROR_STRIPE_COLOR, "c8c8c8")),
                    new IclsAttributeOption(DIFF_INSERTED, Map.of(BACKGROUND, "bee6be", ERROR_STRIPE_COLOR, "aadeaa")),
                    new IclsAttributeOption(DIFF_MODIFIED, Map.of(BACKGROUND, "cad9fa", ERROR_STRIPE_COLOR, "b8cbf5")),
                    new IclsAttributeOption(DUPLICATE_FROM_SERVER, Map.of(BACKGROUND, "f5f7f0")),
                    new IclsAttributeOption(ERRORS_ATTRIBUTES, Map.of(EFFECT_COLOR, "ff0000", ERROR_STRIPE_COLOR, "cf5b56", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(EXECUTIONPOINT_ATTRIBUTES, Map.of(FOREGROUND, "ffffff", BACKGROUND, "2154a6", ERROR_STRIPE_COLOR, "2154a6")),
                    new IclsAttributeOption(FOLLOWED_HYPERLINK_ATTRIBUTES, Map.of(FOREGROUND, "ff", BACKGROUND, "e9e9e9", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(GENERIC_SERVER_ERROR_OR_WARNING, Map.of(EFFECT_COLOR, "f49810", ERROR_STRIPE_COLOR, "e69317", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(HYPERLINK_ATTRIBUTES, Map.of(FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(INFORMATION_ATTRIBUTES, Map.of()),
                    new IclsAttributeOption(INFO_ATTRIBUTES, Map.of(EFFECT_COLOR, "cccccc", ERROR_STRIPE_COLOR, "d9cfad", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(LINE_FULL_COVERAGE, Map.of(FOREGROUND, "ccffcc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_NONE_COVERAGE, Map.of(FOREGROUND, "ffcccc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_PARTIAL_COVERAGE, Map.of(FOREGROUND, "ffffcc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LOG_ERROR_OUTPUT, Map.of(FOREGROUND, "cd0000")),
                    new IclsAttributeOption(LOG_EXPIRED_ENTRY, Map.of(FOREGROUND, "555555")),
                    new IclsAttributeOption(LOG_WARNING_OUTPUT, Map.of(FOREGROUND, "a66f00")),
                    new IclsAttributeOption(NOT_TOP_FRAME_ATTRIBUTES, Map.of(BACKGROUND, "c0d0f0")),
                    new IclsAttributeOption(NOT_USED_ELEMENT_ATTRIBUTES, Map.of(FOREGROUND, "808080")),
                    new IclsAttributeOption(WARNING_ATTRIBUTES, Map.of(BACKGROUND, "f6ebbc", ERROR_STRIPE_COLOR, "ebc700", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(WRONG_REFERENCES_ATTRIBUTES, Map.of(FOREGROUND, "ff0000"))),
            DARK, List.of(
                    new IclsAttributeOption(BAD_CHARACTER, Map.of(BACKGROUND, "ff0000")),
                    new IclsAttributeOption(BREAKPOINT_ATTRIBUTES, Map.of(BACKGROUND, "3a2323", ERROR_STRIPE_COLOR, "664233")),
                    new IclsAttributeOption(CONSOLE_BLACK_OUTPUT, Map.of(FOREGROUND, "ffffff")),
                    new IclsAttributeOption(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(FOREGROUND, "7eaef1")),
                    new IclsAttributeOption(CONSOLE_BLUE_OUTPUT, Map.of(FOREGROUND, "5394ec")),
                    new IclsAttributeOption(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "6cdada")),
                    new IclsAttributeOption(CONSOLE_CYAN_OUTPUT, Map.of(FOREGROUND, "299999")),
                    new IclsAttributeOption(CONSOLE_DARKGRAY_OUTPUT, Map.of(FOREGROUND, "555555")),
                    new IclsAttributeOption(CONSOLE_ERROR_OUTPUT, Map.of(FOREGROUND, "ff6b68")),
                    new IclsAttributeOption(CONSOLE_GRAY_OUTPUT, Map.of(FOREGROUND, "999999")),
                    new IclsAttributeOption(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(FOREGROUND, "a8c023")),
                    new IclsAttributeOption(CONSOLE_GREEN_OUTPUT, Map.of(FOREGROUND, "a8c023")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff99ff")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_OUTPUT, Map.of(FOREGROUND, "ae8abe")),
                    new IclsAttributeOption(CONSOLE_NORMAL_OUTPUT, Map.of(FOREGROUND, "bbbbbb")),
                    new IclsAttributeOption(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ff8785")),
                    new IclsAttributeOption(CONSOLE_RED_OUTPUT, Map.of(FOREGROUND, "ff6b68")),
                    new IclsAttributeOption(CONSOLE_SYSTEM_OUTPUT, Map.of(FOREGROUND, "bbbbbb")),
                    new IclsAttributeOption(CONSOLE_USER_INPUT, Map.of(FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new IclsAttributeOption(CONSOLE_WHITE_OUTPUT, Map.of(FOREGROUND, "1f1f1f")),
                    new IclsAttributeOption(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(FOREGROUND, "ffff00")),
                    new IclsAttributeOption(CONSOLE_YELLOW_OUTPUT, Map.of(FOREGROUND, "d6bf55")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES, Map.of(FOREGROUND, "66d75", FONT_TYPE, "2")),
                    new IclsAttributeOption(DIFF_ABSENT, Map.of(FOREGROUND, "null")),
                    new IclsAttributeOption(DIFF_CONFLICT, Map.of(BACKGROUND, "45302b")),
                    new IclsAttributeOption(DIFF_DELETED, Map.of(BACKGROUND, "484a4a")),
                    new IclsAttributeOption(DIFF_INSERTED, Map.of(BACKGROUND, "294436")),
                    new IclsAttributeOption(DIFF_MODIFIED, Map.of(BACKGROUND, "385570")))
    );

    private static void updateAttributeValueFontOption(IclsAttributeOption iclsAttributeOption, EclipseColorThemeElement eclipseSetting) {
        IclsFontBasicType fontBasicType = IclsFontBasicType.of(eclipseSetting.isBold(), eclipseSetting.isItalic());
        Map<IclsOptionProperty, String> iclsAttributeOptionValue = iclsAttributeOption.getValues();
        if (iclsAttributeOptionValue.containsKey(FONT_TYPE)) {
            if ((eclipseSetting.isBoldSetFalse() || eclipseSetting.isItalicSetFalse()) && iclsAttributeOption.getName() != DEFAULT_VALID_STRING_ESCAPE) {
                iclsAttributeOption.removeAttributeIclsOptionPropertyValue(FONT_TYPE);

            }
        }

        if (fontBasicType != IclsFontBasicType.NORMAL && iclsAttributeOption.getName() != DEFAULT_VALID_STRING_ESCAPE)
            iclsAttributeOption.addAttributeIclsOptionPropertyValue(FONT_TYPE, fontBasicType.toString());
    }

    @Override
    public IntellijIdeaColorScheme convert(EclipseColorTheme eclipseColorTheme) {
        Map<IntellijIdeaColorScheme.ColorOption, String> iclsColorOptions = new HashMap<>();
        Map<IntellijIdeaColorScheme.AttributeOptionName, IclsAttributeOption> attributeOptionsValuesByName = ICLS_CONSOLE_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).stream()
                .collect(Collectors.toMap(IclsAttributeOption::getName, iclsAttributeOption -> iclsAttributeOption));

        for (Map.Entry<EclipseColorTheme.SettingField, EclipseColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            EclipseColorTheme.SettingField eclipseFieldName = colorThemeElement.getKey();
            EclipseColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<IntellijIdeaColorScheme.ColorOption> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (IntellijIdeaColorScheme.ColorOption iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            }
        }

        String eclipseForegroundHex = ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS.get(eclipseColorTheme.getSettingsByName().get(foreground).getColorValue());
        for (IntellijIdeaColorScheme.ColorOption iclsColorOption : ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_OPTIONS) {
            iclsColorOptions.put(iclsColorOption, eclipseForegroundHex);
        }

        for (Map.Entry<IntellijIdeaColorScheme.AttributeOptionName, Map<IclsOptionProperty, EclipseColorTheme.SettingField>> iclsMappedEclipseAttribute : ECLIPSE_TO_ICLS_ATTRIBUTES.entrySet()) {
            IntellijIdeaColorScheme.AttributeOptionName attributeOptionName = iclsMappedEclipseAttribute.getKey();
            Map<IclsOptionProperty, EclipseColorTheme.SettingField> eclipseMappedIclsOptionProperties = iclsMappedEclipseAttribute.getValue();
            for (Map.Entry<IclsOptionProperty, EclipseColorTheme.SettingField> iclsAttributeIclsOptionPropertyValue : eclipseMappedIclsOptionProperties.entrySet()) {
                IclsOptionProperty IclsOptionPropertyName = iclsAttributeIclsOptionPropertyValue.getKey();
                EclipseColorThemeElement eclipseSetting = eclipseColorTheme.getSettingByName(iclsAttributeIclsOptionPropertyValue.getValue());
                if (eclipseSetting != null) {
                    String IclsOptionPropertyValue = eclipseSetting.getColorValue();
                    if (attributeOptionName.colorMapper != null)
                        IclsOptionPropertyValue = attributeOptionName.colorMapper.get(IclsOptionPropertyValue);


                    if (!attributeOptionsValuesByName.containsKey(attributeOptionName))
                        attributeOptionsValuesByName.put(attributeOptionName, new IclsAttributeOption(attributeOptionName, new HashMap<>()));
                    IclsAttributeOption iclsAttributeOption = attributeOptionsValuesByName.get(attributeOptionName);

                    iclsAttributeOption.addAttributeIclsOptionPropertyValue(IclsOptionPropertyName, IclsOptionPropertyValue);
                    updateAttributeValueFontOption(iclsAttributeOption, eclipseSetting);

                    if (eclipseSetting.isUnderline()) {
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_COLOR, IclsOptionPropertyValue);
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value);
                    } else if (eclipseSetting.isStrikethrough()) {
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_COLOR, IclsOptionPropertyValue);
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_TYPE, IclsFontEffectType.STRIKETHROUGH.value);
                    }
                }
            }
        }

        return new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                attributeOptionsValuesByName.values());
    }
}