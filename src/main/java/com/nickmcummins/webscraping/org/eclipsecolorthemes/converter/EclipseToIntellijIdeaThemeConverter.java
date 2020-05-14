package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.AttributeOptionValues;
import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.SchemeType;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.ColorThemeElement;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

import java.util.*;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.AttributeOption.*;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ColorOption.*;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.OptionProperty;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.OptionProperty.*;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.SettingField;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.SettingField.*;
import static java.util.Map.entry;

public class EclipseToIntellijIdeaThemeConverter implements ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> {
    public static final Map<SettingField, List<IntellijIdeaColorScheme.ColorOption>> ECLIPSE_TO_IDEA_OPTIONS = Map.of(
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
            entry(occurrenceIndication, List.of(IDENTIFIER_UNDER_CARET_ATTRIBUTES)),
            entry(writeOccurrenceIndication, List.of(WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES)),
            entry(singleLineComment, List.of(DEFAULT_LINE_COMMENT)),
            entry(multiLineComment, List.of(DEFAULT_BLOCK_COMMENT)),
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
            entry(stringColor, List.of(DEFAULT_VALID_STRING_ESCAPE)),
            entry(operator, List.of(DEFAULT_DOT, DEFAULT_SEMICOLON, DEFAULT_OPERATION_SIGN, DEFAULT_COMMA)),
            entry(keyword, List.of(DEFAULT_KEYWORD, DEFAULT_ENTITY)),
            entry(annotation, List.of(DEFAULT_METADATA)),
            entry(staticMethod, List.of(DEFAULT_STATIC_METHOD)),
            entry(localVariableDeclaration, List.of(DEFAULT_LOCAL_VARIABLE)),
            entry(field, List.of(DEFAULT_INSTANCE_FIELD)),
            entry(staticFinalField, List.of(STATIC_FINAL_FIELD_ATTRIBUTES)),
            entry(enumColor, List.of(ENUM_NAME_ATTRIBUTES)),
            entry(inheritedMethod, List.of(INHERITED_METHOD_ATTRIBUTES)),
            entry(abstractMethod, List.of(ABSTRACT_METHOD_ATTRIBUTES)),
            entry(typeParameter, List.of(TYPE_PARAMETER_NAME_ATTRIBUTES)),
            entry(constant, List.of(DEFAULT_CONSTANT))
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
                    new AttributeOptionValues(DEBUGGER_INLINED_VALUES, Map.of(FOREGROUND, "868686", FONT_TYPE, "2")),
                    new AttributeOptionValues(DIFF_ABSENT, Map.of(FOREGROUND, "f0f0f0")),
                    new AttributeOptionValues(DIFF_CONFLICT, Map.of(BACKGROUND, "ffd5cc")),
                    new AttributeOptionValues(DIFF_DELETED, Map.of(BACKGROUND, "d6d6d6")),
                    new AttributeOptionValues(DIFF_INSERTED, Map.of(BACKGROUND, "bee6be")),
                    new AttributeOptionValues(DIFF_MODIFIED, Map.of(BACKGROUND, "cad9fa"))),
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

        for (Map.Entry<SettingField, ColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            SettingField eclipseFieldName = colorThemeElement.getKey();
            ColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<IntellijIdeaColorScheme.ColorOption> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (IntellijIdeaColorScheme.ColorOption iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            } else if (ECLIPSE_TO_IDEA_ATTRIBUTES.containsKey(eclipseFieldName)) {
                for (IntellijIdeaColorScheme.AttributeOption iclsAttributeOptionName : ECLIPSE_TO_IDEA_ATTRIBUTES.get(eclipseFieldName)) {
                    Map<OptionProperty, String> attributeOptions = new HashMap<>();
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

        iclsAttributeOptions.addAll(ICLS_CONSOLE_DEFAULTS.get(eclipseColorTheme.getLightOrDark()));

        return new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                iclsAttributeOptions);
    }
}