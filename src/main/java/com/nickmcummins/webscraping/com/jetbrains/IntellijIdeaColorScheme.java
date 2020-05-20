package com.nickmcummins.webscraping.com.jetbrains;

import com.nickmcummins.webscraping.ColorTheme;
import org.jsoup.internal.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.created;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.modified;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.originalScheme;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.OptionProperty.*;

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
    public enum AttributeOption {
        ABSTRACT_METHOD_ATTRIBUTES,
        BAD_CHARACTER,
        BREAKPOINT_ATTRIBUTES,
        CONSOLE_BLACK_OUTPUT,
        CONSOLE_BLUE_BRIGHT_OUTPUT,
        CONSOLE_BLUE_OUTPUT,
        CONSOLE_CYAN_BRIGHT_OUTPUT,
        CONSOLE_CYAN_OUTPUT,
        CONSOLE_DARKGRAY_OUTPUT,
        CONSOLE_ERROR_OUTPUT,
        CONSOLE_GRAY_OUTPUT,
        CONSOLE_GREEN_BRIGHT_OUTPUT,
        CONSOLE_GREEN_OUTPUT,
        CONSOLE_MAGENTA_BRIGHT_OUTPUT,
        CONSOLE_MAGENTA_OUTPUT,
        CONSOLE_NORMAL_OUTPUT,
        CONSOLE_RED_BRIGHT_OUTPUT,
        CONSOLE_RED_OUTPUT,
        CONSOLE_SYSTEM_OUTPUT,
        CONSOLE_USER_INPUT,
        CONSOLE_WHITE_OUTPUT,
        CONSOLE_YELLOW_BRIGHT_OUTPUT,
        CONSOLE_YELLOW_OUTPUT,
        CTRL_CLICKABLE,
        DEBUGGER_INLINED_VALUES,
        DEBUGGER_INLINED_VALUES_EXECUTION_LINE,
        DEBUGGER_INLINED_VALUES_MODIFIED,
        DEFAULT_BRACES,
        DEFAULT_BRACKETS,
        DEFAULT_COMMA,
        DEFAULT_CONSTANT,
        DEFAULT_CLASS_NAME,
        DEFAULT_DOT,
        DEFAULT_ENTITY,
        DEFAULT_IDENTIFIER,
        DEFAULT_INSTANCE_FIELD,
        DEFAULT_INTERFACE_NAME,
        DEFAULT_INVALID_STRING_ESCAPE,
        DEFAULT_KEYWORD,
        DEFAULT_FUNCTION_CALL,
        DEFAULT_FUNCTION_DECLARATION,
        DEFAULT_BLOCK_COMMENT,
        DEFAULT_DOC_COMMENT,
        DEFAULT_DOC_COMMENT_TAG,
        DEFAULT_DOC_MARKUP,
        DEFAULT_LINE_COMMENT,
        DEFAULT_LOCAL_VARIABLE,
        DEFAULT_METADATA,
        DEFAULT_NUMBER,
        DEFAULT_OPERATION_SIGN,
        DEFAULT_PARAMETER,
        DEFAULT_PARENTHS,
        DEFAULT_SEMICOLON,
        DEFAULT_STATIC_FIELD,
        DEFAULT_STATIC_METHOD,
        DEFAULT_STRING,
        DEFAULT_TAG,
        DEFAULT_VALID_STRING_ESCAPE,
        DEPRECATED_ATTRIBUTES,
        DIFF_ABSENT,
        DIFF_CONFLICT,
        DIFF_DELETED,
        DIFF_INSERTED,
        DIFF_MODIFIED,
        DUPLICATE_FROM_SERVER,
        ERRORS_ATTRIBUTES,
        ENUM_NAME_ATTRIBUTES,
        EXECUTIONPOINT_ATTRIBUTES,
        FOLLOWED_HYPERLINK_ATTRIBUTES,
        GENERIC_SERVER_ERROR_OR_WARNING,
        HTML_ATTRIBUTE_NAME,
        HTML_TAG_NAME,
        HYPERLINK_ATTRIBUTES,
        IDENTIFIER_UNDER_CARET_ATTRIBUTES,
        INFORMATION_ATTRIBUTES,
        INFO_ATTRIBUTES,
        INHERITED_METHOD_ATTRIBUTES,
        LINE_FULL_COVERAGE,
        LINE_NONE_COVERAGE,
        LINE_PARTIAL_COVERAGE,
        LOG_ERROR_OUTPUT,
        LOG_EXPIRED_ENTRY,
        LOG_WARNING_OUTPUT,
        MATCHED_BRACE_ATTRIBUTES,
        NOT_TOP_FRAME_ATTRIBUTES,
        NOT_USED_ELEMENT_ATTRIBUTES,
        SEARCH_RESULT_ATTRIBUTES,
        STATIC_FINAL_FIELD_ATTRIBUTES,
        TEXT,
        TEXT_SEARCH_RESULT_ATTRIBUTES,
        TODO_DEFAULT_ATTRIBUTES,
        TYPE_PARAMETER_NAME_ATTRIBUTES,
        WARNING_ATTRIBUTES,
        WRITE_IDENTIFIER_UNDER_CARET_ATTRIBUTES,
        WRONG_REFERENCES_ATTRIBUTES,
        XML_ATTRIBUTE_NAME,
        XML_TAG_NAME
    }

    public enum OptionProperty {
        EFFECT_COLOR,
        ERROR_STRIPE_COLOR,
        EFFECT_TYPE,
        FOREGROUND,
        FONT_TYPE,
        BACKGROUND
    }
    public enum FontType {
        NORMAL("0"),
        BOLD("1"),
        ITALIC("2"),
        BOLD_ITALIC("3");

        private static final Map<String, FontType> BY_VALUES = Arrays.stream(FontType.values())
                .collect(Collectors.toMap(fontType -> fontType.value, fontType -> fontType));
        private final String value;

        FontType(String value) {
            this.value = value;
        }

        public int toNumber() {
            return Integer.parseInt(value);
        }

        public String toString() {
            return value;
        }

        public static FontType fromNumeric(int numeric) {
            return BY_VALUES.get(Integer.toString(numeric));
        }
    }

    private static final String EXTENSION = "icls";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final List<OptionProperty> ATTRIBUTE_OPTION_VALUE_ORDER = List.of(FOREGROUND, FONT_TYPE, BACKGROUND,
            EFFECT_COLOR, ERROR_STRIPE_COLOR, EFFECT_TYPE);
    private final Map<MetaInfoProperty, String> metaInfo;
    private final Map<ColorOption, String> colorOptions;
    private final List<AttributeOptionValues> attributeOptions;

    public IntellijIdeaColorScheme(Map<MetaInfoProperty, String> metaInfo, Map<ColorOption, String> colorOptions, List<AttributeOptionValues> attributeOptions) {
        this.metaInfo = metaInfo;
        this.colorOptions = colorOptions;
        this.attributeOptions = attributeOptions;
    }

    public IntellijIdeaColorScheme(String createdValue, String name, Map<ColorOption, String> colorOptions, List<AttributeOptionValues> attributeOptions) {
        this(Map.of(
                created, DATE_FORMAT.format(StringUtil.isBlank(createdValue) ? System.currentTimeMillis() : createdValue),
                modified, DATE_FORMAT.format(System.currentTimeMillis()),
                originalScheme, name)
                        .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
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
                .map(colorOption -> String.format("<option name=\"%s\" value=\"%s\" />", colorOption.getKey(), colorOption.getValue()))
                .collect(Collectors.joining("\n        "));
        String attributesOptions = attributeOptions.stream()
                .sorted(Comparator.comparing(attributeOption -> attributeOption.getAttributeOption().toString()))
                .map(AttributeOptionValues::toString)
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
