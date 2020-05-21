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
    public static final Map<IntellijIdeaColorScheme.AttributeOption, Map<IntellijIdeaColorScheme.OptionProperty, EclipseColorTheme.SettingField>> ECLIPSE_TO_ICLS_ATTRIBUTES = Map.ofEntries(
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
            entry(DEFAULT_ENTITY, Map.of(FOREGROUND, localVariableDeclaration)),
            entry(DEFAULT_FUNCTION_CALL, Map.of(FOREGROUND, method)),
            entry(DEFAULT_FUNCTION_DECLARATION, Map.of(FOREGROUND, methodDeclaration)),
            entry(DEFAULT_INTERFACE_NAME, Map.of(FOREGROUND, interfaceColor)),
            entry(DEFAULT_INSTANCE_FIELD, Map.of(FOREGROUND, field)),
            entry(DEFAULT_IDENTIFIER, Map.of(FOREGROUND, foreground, BACKGROUND, background)),
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
            entry(LOG_EXPIRED_ENTRY, Map.of(FOREGROUND, foreground)),
            entry(STATIC_FINAL_FIELD_ATTRIBUTES, Map.of(FOREGROUND, staticFinalField)),
            entry(TEXT, Map.of(FOREGROUND, foreground, BACKGROUND, background)),
            entry(TODO_DEFAULT_ATTRIBUTES, Map.of(FOREGROUND, commentTaskTag)),
            entry(TYPE_PARAMETER_NAME_ATTRIBUTES, Map.of(FOREGROUND, typeParameter)),
            entry(WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES, Map.of(BACKGROUND, writeOccurrenceIndication)),
            entry(XML_ATTRIBUTE_NAME, Map.of(FOREGROUND, field)),
            entry(XML_TAG_NAME, Map.of(FOREGROUND, keyword))
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
                    new AttributeOptionValues(DEFAULT_ENTITY, Map.of(FONT_TYPE, "1")),
                    new AttributeOptionValues(DEFAULT_INVALID_STRING_ESCAPE, Map.of(FOREGROUND, "8000", BACKGROUND, "ffcccc")),
                    new AttributeOptionValues(DEFAULT_KEYWORD, Map.of(FONT_TYPE, "1")),
                    new AttributeOptionValues(DEFAULT_VALID_STRING_ESCAPE, Map.of(FONT_TYPE, "1")),
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
                    new AttributeOptionValues(HYPERLINK_ATTRIBUTES, Map.of(FOREGROUND, "ff", EFFECT_COLOR, "ff", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(INFORMATION_ATTRIBUTES, Map.of()),
                    new AttributeOptionValues(INFO_ATTRIBUTES, Map.of(EFFECT_COLOR, "cccccc", ERROR_STRIPE_COLOR, "d9cfad", EFFECT_TYPE, "2")),
                    new AttributeOptionValues(LINE_FULL_COVERAGE, Map.of(FOREGROUND, "ccffcc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LINE_NONE_COVERAGE, Map.of(FOREGROUND, "ffcccc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LINE_PARTIAL_COVERAGE, Map.of(FOREGROUND, "ffffcc", FONT_TYPE, "1")),
                    new AttributeOptionValues(LOG_ERROR_OUTPUT, Map.of(FOREGROUND, "cd0000")),
                    new AttributeOptionValues(LOG_WARNING_OUTPUT, Map.of(FOREGROUND, "a66f00")),
                    new AttributeOptionValues(MATCHED_BRACE_ATTRIBUTES, Map.of(EFFECT_COLOR, "a8a8a8")),
                    new AttributeOptionValues(NOT_TOP_FRAME_ATTRIBUTES, Map.of(BACKGROUND, "c0d0f0")),
                    new AttributeOptionValues(NOT_USED_ELEMENT_ATTRIBUTES, Map.of(FOREGROUND, "808080")),
                    new AttributeOptionValues(SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, "dddddd")),
                    new AttributeOptionValues(TEXT_SEARCH_RESULT_ATTRIBUTES, Map.of(BACKGROUND, "dddddd")),
                    new AttributeOptionValues(WARNING_ATTRIBUTES, Map.of(BACKGROUND, "f6ebbc", ERROR_STRIPE_COLOR, "ebc700", EFFECT_TYPE, "1")),
                    new AttributeOptionValues(WRONG_REFERENCES_ATTRIBUTES, Map.of(FOREGROUND, "ff0000"))),

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
    
    private static IntellijIdeaColorScheme.FontType iclsFontTypeFromEclipseSetting(ColorThemeElement eclipseSetting) {
        int fontType = IntellijIdeaColorScheme.FontType.NORMAL.toNumber();
        if (eclipseSetting.isBold())
            fontType += IntellijIdeaColorScheme.FontType.BOLD.toNumber();
        if (eclipseSetting.isItalic())
            fontType += IntellijIdeaColorScheme.FontType.ITALIC.toNumber();
        
        return IntellijIdeaColorScheme.FontType.fromNumeric(fontType); 
    }

    @Override
    public IntellijIdeaColorScheme convert(EclipseColorTheme eclipseColorTheme) {
        Map<IntellijIdeaColorScheme.ColorOption, String> iclsColorOptions = new HashMap<>();
        Map<IntellijIdeaColorScheme.AttributeOption, AttributeOptionValues> attributeOptionsValuesByName = ICLS_CONSOLE_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).stream()
                .collect(Collectors.toMap(AttributeOptionValues::getAttributeOption, iclsAttributeOption -> iclsAttributeOption));

        for (Map.Entry<EclipseColorTheme.SettingField, ColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            EclipseColorTheme.SettingField eclipseFieldName = colorThemeElement.getKey();
            ColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<IntellijIdeaColorScheme.ColorOption> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (IntellijIdeaColorScheme.ColorOption iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            }
        }

        for (Map.Entry<IntellijIdeaColorScheme.ColorOption, String> colorOptionDefault : ICLS_COLOR_OPTION_DEFAULTS.get(eclipseColorTheme.getLightOrDark()).entrySet()) {
            iclsColorOptions.put(colorOptionDefault.getKey(), colorOptionDefault.getValue());
        }

        for (Map.Entry<IntellijIdeaColorScheme.AttributeOption, Map<IntellijIdeaColorScheme.OptionProperty, EclipseColorTheme.SettingField>> iclsMappedEclipseAttribute : ECLIPSE_TO_ICLS_ATTRIBUTES.entrySet()) {
            IntellijIdeaColorScheme.AttributeOption attributeOptionName = iclsMappedEclipseAttribute.getKey();
            Map<IntellijIdeaColorScheme.OptionProperty, EclipseColorTheme.SettingField> eclipseMappedIclsOptionProperties = iclsMappedEclipseAttribute.getValue();
            for (Map.Entry<IntellijIdeaColorScheme.OptionProperty, EclipseColorTheme.SettingField> iclsAttributeOptionPropertyValue : eclipseMappedIclsOptionProperties.entrySet()) {
                IntellijIdeaColorScheme.OptionProperty optionPropertyName = iclsAttributeOptionPropertyValue.getKey();
                ColorThemeElement eclipseSetting = eclipseColorTheme.getSettingsByName().get(iclsAttributeOptionPropertyValue.getValue());
                String optionPropertyValue = eclipseSetting.getColorValue();
                if (!attributeOptionsValuesByName.containsKey(attributeOptionName))
                    attributeOptionsValuesByName.put(attributeOptionName, new AttributeOptionValues(attributeOptionName, new HashMap<>()));
                AttributeOptionValues attributeOptionValues = attributeOptionsValuesByName.get(attributeOptionName);
                attributeOptionValues.addAttributeOptionPropertyValue(optionPropertyName, optionPropertyValue);

                IntellijIdeaColorScheme.FontType fontType = iclsFontTypeFromEclipseSetting(eclipseSetting);
                if (fontType != IntellijIdeaColorScheme.FontType.NORMAL)
                    attributeOptionValues.addAttributeOptionPropertyValue(FONT_TYPE, fontType.toString());
                if (eclipseSetting.isStrikethrough())
                    attributeOptionValues.addAttributeOptionPropertyValue(EFFECT_TYPE, "3");
            }
        }

        List<AttributeOptionValues> iclsAttributeOptions = Arrays.asList(attributeOptionsValuesByName.values().toArray(new AttributeOptionValues[0]));
        return new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                iclsAttributeOptions);
    }
}