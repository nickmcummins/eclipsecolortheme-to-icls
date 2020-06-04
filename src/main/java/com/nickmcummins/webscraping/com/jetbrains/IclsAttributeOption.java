package com.nickmcummins.webscraping.com.jetbrains;

import org.jsoup.nodes.Element;

import java.util.*;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.formatHexValue;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.ATTRIBUTE_OPTION_VALUE_ORDER;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.converter.EclipseToIntellijIdeaThemeConverter.ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS;

public class IclsAttributeOption {
    public enum Name {
        ABSTRACT_METHOD_ATTRIBUTES, BAD_CHARACTER, BREAKPOINT_ATTRIBUTES,
        CONSOLE_BLACK_OUTPUT, CONSOLE_BLUE_BRIGHT_OUTPUT, CONSOLE_BLUE_OUTPUT, CONSOLE_CYAN_BRIGHT_OUTPUT,
        CONSOLE_CYAN_OUTPUT, CONSOLE_DARKGRAY_OUTPUT, CONSOLE_ERROR_OUTPUT, CONSOLE_GRAY_OUTPUT,
        CONSOLE_GREEN_BRIGHT_OUTPUT, CONSOLE_GREEN_OUTPUT, CONSOLE_MAGENTA_BRIGHT_OUTPUT, CONSOLE_MAGENTA_OUTPUT,
        CONSOLE_NORMAL_OUTPUT, CONSOLE_RED_BRIGHT_OUTPUT, CONSOLE_RED_OUTPUT, CONSOLE_SYSTEM_OUTPUT, CONSOLE_USER_INPUT,
        CONSOLE_WHITE_OUTPUT, CONSOLE_YELLOW_BRIGHT_OUTPUT, CONSOLE_YELLOW_OUTPUT,
        CTRL_CLICKABLE,
        DEBUGGER_INLINED_VALUES, DEBUGGER_INLINED_VALUES_EXECUTION_LINE, DEBUGGER_INLINED_VALUES_MODIFIED,
        DEFAULT_BRACES, DEFAULT_BRACKETS, DEFAULT_COMMA, DEFAULT_CONSTANT, DEFAULT_CLASS_NAME, DEFAULT_DOT,
        DEFAULT_ENTITY, DEFAULT_IDENTIFIER, DEFAULT_INSTANCE_FIELD, DEFAULT_INTERFACE_NAME,
        DEFAULT_INVALID_STRING_ESCAPE, DEFAULT_KEYWORD, DEFAULT_FUNCTION_CALL, DEFAULT_FUNCTION_DECLARATION,
        DEFAULT_BLOCK_COMMENT, DEFAULT_DOC_COMMENT, DEFAULT_DOC_COMMENT_TAG, DEFAULT_DOC_MARKUP, DEFAULT_LINE_COMMENT,
        DEFAULT_LOCAL_VARIABLE, DEFAULT_METADATA, DEFAULT_NUMBER, DEFAULT_OPERATION_SIGN, DEFAULT_PARAMETER,
        DEFAULT_PARENTHS, DEFAULT_SEMICOLON, DEFAULT_STATIC_FIELD, DEFAULT_STATIC_METHOD, DEFAULT_STRING, DEFAULT_TAG,
        DEFAULT_VALID_STRING_ESCAPE,
        DEPRECATED_ATTRIBUTES,
        DIFF_ABSENT, DIFF_CONFLICT, DIFF_DELETED, DIFF_INSERTED, DIFF_MODIFIED,
        DUPLICATE_FROM_SERVER,
        ERRORS_ATTRIBUTES, ENUM_NAME_ATTRIBUTES, EXECUTIONPOINT_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES,
        GENERIC_SERVER_ERROR_OR_WARNING,
        HTML_ATTRIBUTE_NAME, HTML_TAG_NAME,
        HYPERLINK_ATTRIBUTES, IDENTIFIER_UNDER_CARET_ATTRIBUTES, INFORMATION_ATTRIBUTES, INFO_ATTRIBUTES, INHERITED_METHOD_ATTRIBUTES,
        LINE_FULL_COVERAGE, LINE_NONE_COVERAGE, LINE_PARTIAL_COVERAGE,
        LOG_ERROR_OUTPUT, LOG_EXPIRED_ENTRY, LOG_WARNING_OUTPUT,
        MATCHED_BRACE_ATTRIBUTES(ECLIPSE_FOREGROUND_TO_MAPPED_ICLS_COLORS),
        NOT_TOP_FRAME_ATTRIBUTES,
        NOT_USED_ELEMENT_ATTRIBUTES,
        SEARCH_RESULT_ATTRIBUTES,
        STATIC_FINAL_FIELD_ATTRIBUTES,
        TEXT, TEXT_SEARCH_RESULT_ATTRIBUTES,
        TODO_DEFAULT_ATTRIBUTES,
        TYPE_PARAMETER_NAME_ATTRIBUTES,
        WARNING_ATTRIBUTES,
        WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES,
        WRONG_REFERENCES_ATTRIBUTES,
        XML_ATTRIBUTE_NAME, XML_TAG_NAME;

        public final Map<String, String> colorMapper;

        Name(Map<String, String> colorMapper) {
            this.colorMapper = colorMapper;
        }

        Name() {
            this(null);
        }
    }

    private final Name name;
    private Map<IclsOptionProperty, String> values;

    public IclsAttributeOption(Name name, Map<IclsOptionProperty, String> values) {
        this.name = name;
        this.values = values;
    }

    public static IclsAttributeOption fromXml(Element attributeOption) {
        return new IclsAttributeOption(
                Name.valueOf(attributeOption.attr("name")),
                attributeOption.select("value").select("option").stream()
                        .collect(Collectors.toMap(option -> IclsOptionProperty.valueOf(option.attr("name")), option -> option.attr("value"))));
    }

    public void addAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName, String value) {
        if (!(values instanceof HashMap))
            values = new HashMap<>(values);

        values.put(IclsOptionPropertyName, value);
    }

    public void removeAttributeIclsOptionPropertyValue(IclsOptionProperty IclsOptionPropertyName) {
        values.remove(IclsOptionPropertyName);
    }

    public Name getName() {
        return name;
    }

    public Map<IclsOptionProperty, String> getValues() {
        return values;
    }

    public String toString() {
        List<String> valueStrings = new ArrayList<>(values.values());
        String formattedValueOptions;
        if (valueStrings.size() == 1 && valueStrings.get(0).equals("null"))
            formattedValueOptions = "<value />";
        else {
            formattedValueOptions = values.entrySet().stream()
                    .sorted(Comparator.comparing(option -> ATTRIBUTE_OPTION_VALUE_ORDER.indexOf(option.getKey())))
                    .map(entry -> String.format("                <option name=\"%s\" value=\"%s\"/>", entry.getKey(), formatHexValue(entry.getValue())))
                    .collect(Collectors.joining("\n"));
        }
        String valuesTag = values.isEmpty() ? "<value/>\n" : String.format("<value>\n%s\n            </value>\n", formattedValueOptions);
        return String.format("        <option name=\"%s\">\n            %s        </option>", name, valuesTag);
    }
}