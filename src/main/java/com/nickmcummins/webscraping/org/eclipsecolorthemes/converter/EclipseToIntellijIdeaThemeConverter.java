package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.*;
import com.nickmcummins.webscraping.SchemeType;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeSettingElement;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeSettingElement.Name;

import java.util.*;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.ColorUtil.listContainsColor;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeSettingElement.Name.*;
import static com.nickmcummins.webscraping.com.jetbrains.IclsAttributeOption.Name.*;
import static com.nickmcummins.webscraping.com.jetbrains.IclsColorOption.Name.*;
import static com.nickmcummins.webscraping.com.jetbrains.IclsOptionProperty.*;

import static java.util.Map.entry;

public class EclipseToIntellijIdeaThemeConverter implements ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> {

    private static final Map<Name, List<IclsColorOption.Name>> ECLIPSE_TO_IDEA_OPTIONS = Map.of(
            Name.BACKGROUND, List.of(CONSOLE_BACKGROUND_KEY, GUTTER_BACKGROUND),
            Name.SELECTION_FOREGROUND, List.of(IclsColorOption.Name.SELECTION_FOREGROUND),
            Name.SELECTION_BACKGROUND, List.of(IclsColorOption.Name.SELECTION_BACKGROUND),
            Name.CURRENT_LINE, List.of(CARET_ROW_COLOR),
            Name.LINE_NUMBER, List.of(CARET_COLOR, LINE_NUMBERS_COLOR, RIGHT_MARGIN_COLOR, TEARLINE_COLOR)
    );
    private static final List<IclsColorOption.Name> ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_OPTIONS = List.of(
            INDENT_GUIDE, SOFT_WRAP_SIGN_COLOR, WHITESPACES
    );
    public static final Map<String, String> ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS = Map.ofEntries(
            entry("#000000", "808080"),
            entry("#007400", "7db87c"),
            entry("#333333", "999999"),
            entry("#430400", "a18280"),
            entry("#555555", "a8a8a8"),
            entry("#525B5B", "a9adad"),
            entry("#585858", "a7a7a7"),
            entry("#777AAA", "4d4e7a"),
            entry("#8000FF", "c080ff"),
            entry("#C0B6A8", "e0dbd4"),
            entry("#EEE6DF", "7d7975"),
            entry("#FF5050", "ffa8a8")
    );
    private static final Map<IclsAttributeOption.Name, Map<IclsOptionProperty, Name>> ICLS_ATTRIBUTE_OPTIONS_FROM_ECLIPSE_SETTING = Map.ofEntries(
            entry(ABSTRACT_METHOD_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, ABSTRACT_METHOD)),
            entry(DEFAULT_BRACES, Map.of(IclsOptionProperty.FOREGROUND, BRACKET)),
            entry(DEFAULT_BRACKETS, Map.of(IclsOptionProperty.FOREGROUND, BRACKET)),
            entry(DEFAULT_BLOCK_COMMENT, Map.of(IclsOptionProperty.FOREGROUND, MULTI_LINE_COMMENT)),
            entry(DEFAULT_COMMA, Map.of(IclsOptionProperty.FOREGROUND, OPERATOR)),
            entry(DEFAULT_CONSTANT, Map.of(IclsOptionProperty.FOREGROUND, CONSTANT)),
            entry(DEFAULT_DOC_COMMENT, Map.of(IclsOptionProperty.FOREGROUND, JAVADOC)),
            entry(DEFAULT_DOC_MARKUP, Map.of(IclsOptionProperty.FOREGROUND, JAVADOC_TAG)),
            entry(DEFAULT_DOC_COMMENT_TAG, Map.of(IclsOptionProperty.FOREGROUND, JAVADOC_KEYWORD)),
            entry(DEFAULT_DOT, Map.of(IclsOptionProperty.FOREGROUND, OPERATOR)),
            entry(DEFAULT_CLASS_NAME, Map.of(IclsOptionProperty.FOREGROUND, CLASS)),
            entry(DEFAULT_ENTITY, Map.of(IclsOptionProperty.FOREGROUND, KEYWORD)),
            entry(DEFAULT_FUNCTION_CALL, Map.of(IclsOptionProperty.FOREGROUND, METHOD)),
            entry(DEFAULT_FUNCTION_DECLARATION, Map.of(IclsOptionProperty.FOREGROUND, METHOD_DECLARATION)),
            entry(DEFAULT_INTERFACE_NAME, Map.of(IclsOptionProperty.FOREGROUND, INTERFACE)),
            entry(DEFAULT_INSTANCE_FIELD, Map.of(IclsOptionProperty.FOREGROUND, FIELD)),
            entry(DEFAULT_IDENTIFIER, Map.of(IclsOptionProperty.FOREGROUND, EclipseColorThemeSettingElement.Name.FOREGROUND, IclsOptionProperty.BACKGROUND, EclipseColorThemeSettingElement.Name.BACKGROUND)),
            entry(DEFAULT_KEYWORD, Map.of(IclsOptionProperty.FOREGROUND, KEYWORD)),
            entry(DEFAULT_LINE_COMMENT, Map.of(IclsOptionProperty.FOREGROUND, SINGLE_LINE_COMMENT)),
            entry(DEFAULT_LOCAL_VARIABLE, Map.of(IclsOptionProperty.FOREGROUND, LOCAL_VARIABLE)),
            entry(DEFAULT_PARAMETER, Map.of(IclsOptionProperty.FOREGROUND, PARAMETER_VARIABLE)),
            entry(DEFAULT_METADATA, Map.of(IclsOptionProperty.FOREGROUND, ANNOTATION)),
            entry(DEFAULT_NUMBER, Map.of(IclsOptionProperty.FOREGROUND, NUMBER)),
            entry(DEFAULT_OPERATION_SIGN, Map.of(IclsOptionProperty.FOREGROUND, OPERATOR)),
            entry(DEFAULT_PARENTHS, Map.of(IclsOptionProperty.FOREGROUND, BRACKET)),
            entry(DEFAULT_SEMICOLON, Map.of(IclsOptionProperty.FOREGROUND, OPERATOR)),
            entry(DEFAULT_STATIC_FIELD, Map.of(IclsOptionProperty.FOREGROUND, STATIC_FIELD)),
            entry(DEFAULT_STATIC_METHOD, Map.of(IclsOptionProperty.FOREGROUND, STATIC_METHOD)),
            entry(DEFAULT_STRING, Map.of(IclsOptionProperty.FOREGROUND, STRING)),
            entry(DEFAULT_TAG, Map.of(IclsOptionProperty.FOREGROUND, LOCAL_VARIABLE)),
            entry(DEFAULT_VALID_STRING_ESCAPE, Map.of(IclsOptionProperty.FOREGROUND, STRING)),
            entry(ENUM_NAME_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, ENUM)),
            entry(HTML_ATTRIBUTE_NAME, Map.of(IclsOptionProperty.FOREGROUND, FIELD)),
            entry(HTML_TAG_NAME, Map.of(IclsOptionProperty.FOREGROUND, LOCAL_VARIABLE_DECLARATION)),
            entry(IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, OCCURRENCE_INDICATION)),
            entry(INHERITED_METHOD_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, INHERITED_METHOD)),
            entry(MATCHED_BRACE_ATTRIBUTES, Map.of(EFFECT_COLOR, Name.FOREGROUND)),
            entry(SEARCH_RESULT_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, SEARCH_RESULT_INDICATION)),
            entry(STATIC_FINAL_FIELD_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, STATIC_FINAL_FIELD)),
            entry(TEXT, Map.of(IclsOptionProperty.FOREGROUND, Name.FOREGROUND, IclsOptionProperty.BACKGROUND, Name.BACKGROUND)),
            entry(TEXT_SEARCH_RESULT_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, FILTERED_SEARCH_RESULT_INDICATION)),
            entry(TODO_DEFAULT_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, COMMENT_TASK_TAG)),
            entry(TYPE_PARAMETER_NAME_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, TYPE_ARGUMENT)),
            entry(WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, WRITE_OCCURRENCE_INDICATION)),
            entry(XML_ATTRIBUTE_NAME, Map.of(IclsOptionProperty.FOREGROUND, FIELD)),
            entry(XML_TAG_NAME, Map.of(IclsOptionProperty.FOREGROUND, LOCAL_VARIABLE_DECLARATION))
    );
    private static final Map<IclsAttributeOption.Name, Map<IclsOptionProperty, List<String>>> ICLS_ATTRIBUTE_OPTIONS_EXCLUDE_VALUES = Map.of(
            DEFAULT_IDENTIFIER, Map.of(IclsOptionProperty.BACKGROUND, List.of("f5f5f5", "ffffff", "f9fcf7", "0b0b0b", "222249"))
    );
    private static final Map<SchemeType, List<IclsAttributeOption>> ICLS_ATTRIBUTE_OPTION_DEFAULTS = Map.of(
            LIGHT, List.of(
                    new IclsAttributeOption(BAD_CHARACTER, Map.of(IclsOptionProperty.BACKGROUND, "ffcccc")),
                    new IclsAttributeOption(BREAKPOINT_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "faeae6", ERROR_STRIPE_COLOR, "ffc8c8")),
                    new IclsAttributeOption(CONSOLE_BLACK_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "0")),
                    new IclsAttributeOption(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "5c5cff")),
                    new IclsAttributeOption(CONSOLE_BLUE_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ee")),
                    new IclsAttributeOption(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ffff")),
                    new IclsAttributeOption(CONSOLE_CYAN_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cccc")),
                    new IclsAttributeOption(CONSOLE_DARKGRAY_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "555555")),
                    new IclsAttributeOption(CONSOLE_ERROR_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "7f0000")),
                    new IclsAttributeOption(CONSOLE_GRAY_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "aaaaaa")),
                    new IclsAttributeOption(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff00")),
                    new IclsAttributeOption(CONSOLE_GREEN_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cd00")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff00ff")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cd00cd")),
                    new IclsAttributeOption(CONSOLE_NORMAL_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "0")),
                    new IclsAttributeOption(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff0000")),
                    new IclsAttributeOption(CONSOLE_RED_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cd0000")),
                    new IclsAttributeOption(CONSOLE_SYSTEM_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "7f")),
                    new IclsAttributeOption(CONSOLE_USER_INPUT, Map.of(IclsOptionProperty.FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new IclsAttributeOption(CONSOLE_WHITE_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ffffff")),
                    new IclsAttributeOption(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "eaea00")),
                    new IclsAttributeOption(CONSOLE_YELLOW_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "c4a000")),
                    new IclsAttributeOption(CTRL_CLICKABLE, Map.of(IclsOptionProperty.FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES, Map.of(IclsOptionProperty.FOREGROUND, "868686", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_EXECUTION_LINE, Map.of(IclsOptionProperty.FOREGROUND, "a9b7d6", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_MODIFIED, Map.of(IclsOptionProperty.FOREGROUND, "ca8021", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEFAULT_ENTITY, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_KEYWORD, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_INVALID_STRING_ESCAPE, Map.of(IclsOptionProperty.FOREGROUND, "8000", IclsOptionProperty.BACKGROUND, "ffcccc")),
                    new IclsAttributeOption(DEFAULT_VALID_STRING_ESCAPE, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEPRECATED_ATTRIBUTES, Map.of(EFFECT_COLOR, "404040", EFFECT_TYPE, IclsFontEffectType.STRIKETHROUGH.value)),
                    new IclsAttributeOption(DIFF_ABSENT, Map.of(IclsOptionProperty.BACKGROUND, "f0f0f0")),
                    new IclsAttributeOption(DIFF_CONFLICT, Map.of(IclsOptionProperty.BACKGROUND, "ffd5cc", ERROR_STRIPE_COLOR, "ffc8bd")),
                    new IclsAttributeOption(DIFF_DELETED, Map.of(IclsOptionProperty.BACKGROUND, "d6d6d6", ERROR_STRIPE_COLOR, "c8c8c8")),
                    new IclsAttributeOption(DIFF_INSERTED, Map.of(IclsOptionProperty.BACKGROUND, "bee6be", ERROR_STRIPE_COLOR, "aadeaa")),
                    new IclsAttributeOption(DIFF_MODIFIED, Map.of(IclsOptionProperty.BACKGROUND, "cad9fa", ERROR_STRIPE_COLOR, "b8cbf5")),
                    new IclsAttributeOption(DUPLICATE_FROM_SERVER, Map.of(IclsOptionProperty.BACKGROUND, "f5f7f0")),
                    new IclsAttributeOption(ERRORS_ATTRIBUTES, Map.of(EFFECT_COLOR, "ff0000", ERROR_STRIPE_COLOR, "cf5b56", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(EXECUTIONPOINT_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "ffffff", IclsOptionProperty.BACKGROUND, "2154a6", ERROR_STRIPE_COLOR, "2154a6")),
                    new IclsAttributeOption(FOLLOWED_HYPERLINK_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "ff", IclsOptionProperty.BACKGROUND, "e9e9e9", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(GENERIC_SERVER_ERROR_OR_WARNING, Map.of(EFFECT_COLOR, "f49810", ERROR_STRIPE_COLOR, "e69317", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(HYPERLINK_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(INFORMATION_ATTRIBUTES, Map.of()),
                    new IclsAttributeOption(INFO_ATTRIBUTES, Map.of(EFFECT_COLOR, "cccccc", ERROR_STRIPE_COLOR, "d9cfad", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(LINE_FULL_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "ccffcc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_NONE_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "ffcccc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_PARTIAL_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "ffffcc", FONT_TYPE, "1")),
                    new IclsAttributeOption(LOG_ERROR_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cd0000")),
                    new IclsAttributeOption(LOG_EXPIRED_ENTRY, Map.of(IclsOptionProperty.FOREGROUND, "555555")),
                    new IclsAttributeOption(LOG_WARNING_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "a66f00")),
                    new IclsAttributeOption(NOT_TOP_FRAME_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "c0d0f0")),
                    new IclsAttributeOption(NOT_USED_ELEMENT_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "808080")),
                    new IclsAttributeOption(WARNING_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "f6ebbc", ERROR_STRIPE_COLOR, "ebc700", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(WRONG_REFERENCES_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "ff0000"))),
            DARK, List.of(
                    new IclsAttributeOption(BAD_CHARACTER, Map.of(IclsOptionProperty.EFFECT_COLOR, "ff0000", IclsOptionProperty.EFFECT_TYPE, IclsFontBasicType.ITALIC.value)),
                    new IclsAttributeOption(BREAKPOINT_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "3a2323", ERROR_STRIPE_COLOR, "664233")),
                    new IclsAttributeOption(CONSOLE_BLACK_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ffffff")),
                    new IclsAttributeOption(CONSOLE_BLUE_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "7eaef1")),
                    new IclsAttributeOption(CONSOLE_BLUE_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "5394ec")),
                    new IclsAttributeOption(CONSOLE_CYAN_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "6cdada")),
                    new IclsAttributeOption(CONSOLE_CYAN_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "299999")),
                    new IclsAttributeOption(CONSOLE_DARKGRAY_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "555555")),
                    new IclsAttributeOption(CONSOLE_ERROR_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff6b68")),
                    new IclsAttributeOption(CONSOLE_GRAY_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "999999")),
                    new IclsAttributeOption(CONSOLE_GREEN_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "a8c023")),
                    new IclsAttributeOption(CONSOLE_GREEN_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "a8c023")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff99ff")),
                    new IclsAttributeOption(CONSOLE_MAGENTA_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ae8abe")),
                    new IclsAttributeOption(CONSOLE_NORMAL_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "bbbbbb")),
                    new IclsAttributeOption(CONSOLE_RED_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff8785")),
                    new IclsAttributeOption(CONSOLE_RED_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ff6b68")),
                    new IclsAttributeOption(CONSOLE_SYSTEM_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "bbbbbb")),
                    new IclsAttributeOption(CONSOLE_USER_INPUT, Map.of(IclsOptionProperty.FOREGROUND, "7f00", FONT_TYPE, "2")),
                    new IclsAttributeOption(CONSOLE_WHITE_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "1f1f1f")),
                    new IclsAttributeOption(CONSOLE_YELLOW_BRIGHT_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "ffff00")),
                    new IclsAttributeOption(CONSOLE_YELLOW_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "d6bf55")),
                    new IclsAttributeOption(CTRL_CLICKABLE, Map.of(IclsOptionProperty.FOREGROUND, "589df6", EFFECT_COLOR, "589df6", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES, Map.of(IclsOptionProperty.FOREGROUND, "666d75", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_EXECUTION_LINE, Map.of(IclsOptionProperty.FOREGROUND, "7b94b3", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEBUGGER_INLINED_VALUES_MODIFIED, Map.of(IclsOptionProperty.FOREGROUND, "ca7e03", FONT_TYPE, "2")),
                    new IclsAttributeOption(DEFAULT_ENTITY, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_KEYWORD, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEFAULT_INVALID_STRING_ESCAPE, Map.of(IclsOptionProperty.FOREGROUND, "6a8759", EFFECT_COLOR, "ff0000", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(DEFAULT_VALID_STRING_ESCAPE, Map.of(FONT_TYPE, IclsFontBasicType.BOLD.value)),
                    new IclsAttributeOption(DEPRECATED_ATTRIBUTES, Map.of(EFFECT_COLOR, "c3c3c3", EFFECT_TYPE, IclsFontEffectType.STRIKETHROUGH.value)),
                    new IclsAttributeOption(DIFF_ABSENT, Map.of(IclsOptionProperty.BACKGROUND, "f0f0f0")),
                    new IclsAttributeOption(DIFF_CONFLICT, Map.of(IclsOptionProperty.BACKGROUND, "45302b", ERROR_STRIPE_COLOR, "8f5247")),
                    new IclsAttributeOption(DIFF_DELETED, Map.of(IclsOptionProperty.BACKGROUND, "484a4a", IclsOptionProperty.ERROR_STRIPE_COLOR, "656e76")),
                    new IclsAttributeOption(DIFF_INSERTED, Map.of(IclsOptionProperty.BACKGROUND, "294436", IclsOptionProperty.ERROR_STRIPE_COLOR, "447152")),
                    new IclsAttributeOption(DIFF_MODIFIED, Map.of(IclsOptionProperty.BACKGROUND, "385570", IclsOptionProperty.ERROR_STRIPE_COLOR, "43698d")),
                    new IclsAttributeOption(DUPLICATE_FROM_SERVER, Map.of(IclsOptionProperty.BACKGROUND, "5e5339")),
                    new IclsAttributeOption(ERRORS_ATTRIBUTES, Map.of(EFFECT_COLOR, "bc3f3c", ERROR_STRIPE_COLOR, "9e2927", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(EXECUTIONPOINT_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "2d6099")),
                    new IclsAttributeOption(FOLLOWED_HYPERLINK_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "287bde", EFFECT_COLOR, "287bde", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(GENERIC_SERVER_ERROR_OR_WARNING, Map.of(EFFECT_COLOR, "f49810", ERROR_STRIPE_COLOR, "b06100", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(HYPERLINK_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "287bde", EFFECT_COLOR, "287bde", EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value)),
                    new IclsAttributeOption(INFORMATION_ATTRIBUTES, Map.of()),
                    new IclsAttributeOption(INFO_ATTRIBUTES, Map.of(EFFECT_COLOR, "aeae80", ERROR_STRIPE_COLOR, "756d56", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(LINE_FULL_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "485848", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_NONE_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "715353", FONT_TYPE, "1")),
                    new IclsAttributeOption(LINE_PARTIAL_COVERAGE, Map.of(IclsOptionProperty.FOREGROUND, "80805a", FONT_TYPE, "1")),
                    new IclsAttributeOption(LOG_ERROR_OUTPUT, Map.of(IclsOptionProperty.FOREGROUND, "cc666e")),
                    new IclsAttributeOption(LOG_EXPIRED_ENTRY, Map.of(IclsOptionProperty.FOREGROUND, "555555")),
                    new IclsAttributeOption(LOG_WARNING_OUTPUT, Map.of()),
                    new IclsAttributeOption(NOT_TOP_FRAME_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "2f3f55")),
                    new IclsAttributeOption(NOT_USED_ELEMENT_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "72737a")),
                    new IclsAttributeOption(WARNING_ATTRIBUTES, Map.of(IclsOptionProperty.BACKGROUND, "52503a", ERROR_STRIPE_COLOR, "be9117", EFFECT_TYPE, IclsFontEffectType.UNDERWAVE.value)),
                    new IclsAttributeOption(WRONG_REFERENCES_ATTRIBUTES, Map.of(IclsOptionProperty.FOREGROUND, "bc3f3c")))
    );

    private static void updateAttributeValueFontOption(IclsAttributeOption iclsAttributeOption, EclipseColorThemeSettingElement eclipseSetting) {
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
        Map<IclsColorOption.Name, String> iclsColorOptions = new HashMap<>();
        Map<IclsAttributeOption.Name, IclsAttributeOption> attributeOptionsValuesByName = ICLS_ATTRIBUTE_OPTION_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).stream()
                .collect(Collectors.toMap(IclsAttributeOption::getName, iclsAttributeOption -> iclsAttributeOption));

        for (Map.Entry<EclipseColorThemeSettingElement.Name, EclipseColorThemeSettingElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            Name eclipseFieldName = colorThemeElement.getKey();
            EclipseColorThemeSettingElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<IclsColorOption.Name> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (IclsColorOption.Name iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            }
        }

        String eclipseForegroundHex = ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS.get(eclipseColorTheme.getSettingsByName().get(EclipseColorThemeSettingElement.Name.FOREGROUND).getColorValue());
        for (IclsColorOption.Name iclsColorOption : ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_OPTIONS) {
            iclsColorOptions.put(iclsColorOption, eclipseForegroundHex);
        }

        for (Map.Entry<IclsAttributeOption.Name, Map<IclsOptionProperty, Name>> iclsMappedEclipseAttribute : ICLS_ATTRIBUTE_OPTIONS_FROM_ECLIPSE_SETTING.entrySet()) {
            IclsAttributeOption.Name attributeOptionName = iclsMappedEclipseAttribute.getKey();
            Map<IclsOptionProperty, Name> eclipseMappedIclsOptionProperties = iclsMappedEclipseAttribute.getValue();
            for (Map.Entry<IclsOptionProperty, EclipseColorThemeSettingElement.Name> iclsAttributeIclsOptionPropertyValue : eclipseMappedIclsOptionProperties.entrySet()) {
                IclsOptionProperty iclsOptionPropertyName = iclsAttributeIclsOptionPropertyValue.getKey();
                EclipseColorThemeSettingElement eclipseSetting = eclipseColorTheme.getSettingByName(iclsAttributeIclsOptionPropertyValue.getValue());
                if (eclipseSetting != null) {
                    String iclsOptionPropertyValue = eclipseSetting.getColorValue();
                    if (attributeOptionName.colorMapper != null)
                        iclsOptionPropertyValue = attributeOptionName.colorMapper.get(iclsOptionPropertyValue);


                    if (!attributeOptionsValuesByName.containsKey(attributeOptionName))
                        attributeOptionsValuesByName.put(attributeOptionName, new IclsAttributeOption(attributeOptionName, new HashMap<>()));
                    IclsAttributeOption iclsAttributeOption = attributeOptionsValuesByName.get(attributeOptionName);
                    if (!(ICLS_ATTRIBUTE_OPTIONS_EXCLUDE_VALUES.containsKey(attributeOptionName)
                            && ICLS_ATTRIBUTE_OPTIONS_EXCLUDE_VALUES.get(attributeOptionName).containsKey(iclsOptionPropertyName)
                            && listContainsColor(ICLS_ATTRIBUTE_OPTIONS_EXCLUDE_VALUES.get(attributeOptionName).get(iclsOptionPropertyName), iclsOptionPropertyValue)))
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(iclsOptionPropertyName, iclsOptionPropertyValue);

                    updateAttributeValueFontOption(iclsAttributeOption, eclipseSetting);

                    if (eclipseSetting.isUnderline()) {
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_COLOR, iclsOptionPropertyValue);
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_TYPE, IclsFontEffectType.UNDERLINE.value);
                    } else if (eclipseSetting.isStrikethrough()) {
                        iclsAttributeOption.addAttributeIclsOptionPropertyValue(EFFECT_COLOR, iclsOptionPropertyValue);
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