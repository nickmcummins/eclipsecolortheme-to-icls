package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.com.jetbrains.AttributeOption;
import com.nickmcummins.webscraping.com.jetbrains.SchemeType;

import java.util.*;

import static com.nickmcummins.webscraping.com.jetbrains.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.com.jetbrains.SchemeType.DARK;
import static java.util.Map.entry;

public class ThemeConverter {
    public static final Map<String, List<String>> ECLIPSE_TO_IDEA_OPTIONS = Map.of(
            "background", List.of("CONSOLE_BACKGROUND_KEY", "GUTTER_BACKGROUND"),
            "selectionForeground", List.of("SELECTION_FOREGROUND"),
            "selectionBackground", List.of("SELECTION_BACKGROUND"),
            "currentLine", List.of("CARET_ROW_COLOR"),
            "lineNumber", List.of("CARET_COLOR", "LINE_NUMBERS_COLOR", "RIGHT_MARGIN_COLOR", "TEARLINE_COLOR")
    );
    public static final Map<String, List<String>> ECLIPSE_TO_IDEA_ATTRIBUTES = Map.ofEntries(
            entry("occurrenceIndication", List.of("IDENTIFIER_UNDER_CARET_ATTRIBUTES")),
            entry("writeOccurrenceIndication", List.of("WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES")),
            entry("singleLineComment", List.of("DEFAULT_LINE_COMMENT")),
            entry("multiLineComment", List.of("DEFAULT_BLOCK_COMMENT")),
            entry("commentTaskTag", List.of("TODO_DEFAULT_ATTRIBUTES")),
            entry("javadoc", List.of("DEFAULT_DOC_COMMENT")),
            entry("javadocTag", List.of("DEFAULT_DOC_MARKUP")),
            entry("javadocKeyword", List.of("DEFAULT_DOC_COMMENT_TAG")),
            entry("class", List.of("DEFAULT_CLASS_NAME")),
            entry("interface", List.of("DEFAULT_INTERFACE_NAME")),
            entry("method", List.of("DEFAULT_FUNCTION_CALL")),
            entry("methodDeclaration", List.of("DEFAULT_FUNCTION_DECLARATION")),
            entry("bracket", List.of("DEFAULT_BRACKETS")),
            entry("number", List.of("DEFAULT_NUMBER")),
            entry("string", List.of("DEFAULT_VALID_STRING_ESCAPE")),
            entry("operator", List.of("DEFAULT_DOT", "DEFAULT_SEMICOLON", "DEFAULT_OPERATION_SIGN", "DEFAULT_COMMA")),
            entry("keyword", List.of("DEFAULT_KEYWORD", "DEFAULT_ENTITY")),
            entry("annotation", List.of("DEFAULT_METADATA")),
            entry("staticMethod", List.of("DEFAULT_STATIC_METHOD")),
            entry("localVariableDeclaration", List.of("DEFAULT_LOCAL_VARIABLE")),
            entry("field", List.of("DEFAULT_INSTANCE_FIELD")),
            entry("staticFinalField", List.of("STATIC_FINAL_FIELD_ATTRIBUTES")),
            entry("enum", List.of("ENUM_NAME_ATTRIBUTES")),
            entry("inheritedMethod", List.of("INHERITED_METHOD_ATTRIBUTES")),
            entry("abstractMethod", List.of("ABSTRACT_METHOD_ATTRIBUTES")),
            entry("typeParameter", List.of("TYPE_PARAMETER_NAME_ATTRIBUTES")),
            entry("constant", List.of("DEFAULT_CONSTANT")
    ));
    public static final Map<SchemeType, List<AttributeOption>> ICLS_CONSOLE_DEFAULTS = Map.of(
            LIGHT, List.of(
                    new AttributeOption("BAD_CHARACTER", Map.of("EFFECT_COLOR", "ffcccc", "EFFECT_TYPE", "2")),
                    new AttributeOption("BREAKPOINT_ATTRIBUTES", Map.of("BACKGROUND", "faeae6", "ERROR_STRIPE_COLOR", "ffc8c8")),
                    new AttributeOption("CONSOLE_BLACK_OUTPUT", Map.of("FOREGROUND", "0")),
                    new AttributeOption("CONSOLE_BLUE_BRIGHT_OUTPUT", Map.of("FOREGROUND", "5c5cff")),
                    new AttributeOption("CONSOLE_BLUE_OUTPUT", Map.of("FOREGROUND", "ee")),
                    new AttributeOption("CONSOLE_CYAN_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ffff")),
                    new AttributeOption("CONSOLE_CYAN_OUTPUT", Map.of("FOREGROUND", "cccc")),
                    new AttributeOption("CONSOLE_DARKGRAY_OUTPUT", Map.of("FOREGROUND", "555555")),
                    new AttributeOption("CONSOLE_ERROR_OUTPUT", Map.of("FOREGROUND", "7f0000")),
                    new AttributeOption("CONSOLE_GRAY_OUTPUT", Map.of("FOREGROUND", "aaaaaa")),
                    new AttributeOption("CONSOLE_GREEN_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ff00")),
                    new AttributeOption("CONSOLE_GREEN_OUTPUT", Map.of("FOREGROUND", "cd00")),
                    new AttributeOption("CONSOLE_MAGENTA_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ff00ff")),
                    new AttributeOption("CONSOLE_MAGENTA_OUTPUT", Map.of("FOREGROUND", "cd00cd")),
                    new AttributeOption("CONSOLE_NORMAL_OUTPUT", Map.of("FOREGROUND", "0")),
                    new AttributeOption("CONSOLE_RED_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ff0000")),
                    new AttributeOption("CONSOLE_RED_OUTPUT", Map.of("FOREGROUND", "cd0000")),
                    new AttributeOption("CONSOLE_SYSTEM_OUTPUT", Map.of("FOREGROUND", "7f")),
                    new AttributeOption("CONSOLE_USER_INPUT", Map.of("FOREGROUND", "7f00", "FONT_TYPE", "2")),
                    new AttributeOption("CONSOLE_WHITE_OUTPUT", Map.of("FOREGROUND", "ffffff")),
                    new AttributeOption("CONSOLE_YELLOW_BRIGHT_OUTPUT", Map.of("FOREGROUND", "eaea00")),
                    new AttributeOption("CONSOLE_YELLOW_OUTPUT", Map.of("FOREGROUND", "c4a000")),
                    new AttributeOption("DEBUGGER_INLINED_VALUES", Map.of("FOREGROUND", "868686", "FONT_TYPE", "2")),
                    new AttributeOption("DIFF_ABSENT", Map.of("FOREGROUND", "f0f0f0")),
                    new AttributeOption("DIFF_CONFLICT", Map.of("BACKGROUND", "ffd5cc")),
                    new AttributeOption("DIFF_DELETED", Map.of("BACKGROUND", "d6d6d6")),
                    new AttributeOption("DIFF_INSERTED", Map.of("BACKGROUND", "bee6be")),
                    new AttributeOption("DIFF_MODIFIED", Map.of("BACKGROUND", "cad9fa"))),
            DARK, List.of(
                    new AttributeOption("BAD_CHARACTER", Map.of("EFFECT_COLOR", "ff0000", "EFFECT_TYPE", "2")),
                    new AttributeOption("BREAKPOINT_ATTRIBUTES", Map.of("BACKGROUND", "3a2323", "ERROR_STRIPE_COLOR", "664233")),
                    new AttributeOption("CONSOLE_BLACK_OUTPUT", Map.of("FOREGROUND", "ffffff")),
                    new AttributeOption("CONSOLE_BLUE_BRIGHT_OUTPUT", Map.of("FOREGROUND", "7eaef1")),
                    new AttributeOption("CONSOLE_BLUE_OUTPUT", Map.of("FOREGROUND", "5394ec")),
                    new AttributeOption("CONSOLE_CYAN_BRIGHT_OUTPUT", Map.of("FOREGROUND", "6cdada")),
                    new AttributeOption("CONSOLE_CYAN_OUTPUT", Map.of("FOREGROUND", "299999")),
                    new AttributeOption("CONSOLE_DARKGRAY_OUTPUT", Map.of("FOREGROUND", "555555")),
                    new AttributeOption("CONSOLE_ERROR_OUTPUT", Map.of("FOREGROUND", "ff6b68")),
                    new AttributeOption("CONSOLE_GRAY_OUTPUT", Map.of("FOREGROUND", "999999")),
                    new AttributeOption("CONSOLE_GREEN_BRIGHT_OUTPUT", Map.of("FOREGROUND", "a8c023")),
                    new AttributeOption("CONSOLE_GREEN_OUTPUT", Map.of("FOREGROUND", "a8c023")),
                    new AttributeOption("CONSOLE_MAGENTA_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ff99ff")),
                    new AttributeOption("CONSOLE_MAGENTA_OUTPUT", Map.of("FOREGROUND", "ae8abe")),
                    new AttributeOption("CONSOLE_NORMAL_OUTPUT", Map.of("FOREGROUND", "bbbbbb")),
                    new AttributeOption("CONSOLE_RED_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ff8785")),
                    new AttributeOption("CONSOLE_RED_OUTPUT", Map.of("FOREGROUND", "ff6b68")),
                    new AttributeOption("CONSOLE_SYSTEM_OUTPUT", Map.of("FOREGROUND", "bbbbbb")),
                    new AttributeOption("CONSOLE_USER_INPUT", Map.of("FOREGROUND", "7f00", "FONT_TYPE", "2")),
                    new AttributeOption("CONSOLE_WHITE_OUTPUT", Map.of("FOREGROUND", "1f1f1f")),
                    new AttributeOption("CONSOLE_YELLOW_BRIGHT_OUTPUT", Map.of("FOREGROUND", "ffff00")),
                    new AttributeOption("CONSOLE_YELLOW_OUTPUT", Map.of("FOREGROUND", "d6bf55")),
                    new AttributeOption("DEBUGGER_INLINED_VALUES", Map.of("FOREGROUND", "66d75", "FONT_TYPE", "2")),
                    new AttributeOption("DIFF_ABSENT", Map.of("FOREGROUND", "null")),
                    new AttributeOption("DIFF_CONFLICT", Map.of("BACKGROUND", "45302b")),
                    new AttributeOption("DIFF_DELETED", Map.of("BACKGROUND", "484a4a")),
                    new AttributeOption("DIFF_INSERTED", Map.of("BACKGROUND", "294436")),
                    new AttributeOption("DIFF_MODIFIED", Map.of("BACKGROUND", "385570")))
    );

    public static String formatHexValue(String hexColor) {
        String formattedHex = hexColor.replaceFirst("#", "").toLowerCase();
        int numZeroDigits = 0;
        int i = 0;
        while (numZeroDigits == i && i < formattedHex.length()) {
            if (formattedHex.charAt(i) == '0')
                numZeroDigits += 1;
            i += 1;
        }

        if (numZeroDigits == i)
            return String.valueOf(formattedHex.charAt(formattedHex.length() - 1));
        else
            return formattedHex.substring(numZeroDigits);
    }
}