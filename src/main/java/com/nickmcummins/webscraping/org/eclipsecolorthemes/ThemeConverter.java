package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeConverter {
    private static final Map<String, String> ECLIPSE_TO_IDEA_ATTRIBUTES;
    static
    {
        Map<String, String> eclipseToIdeaAttributes = new HashMap<>();
        eclipseToIdeaAttributes.put("abstractMethod", "ABSTRACT_METHOD_ATTRIBUTES");
        eclipseToIdeaAttributes.put("multiLineComment", "DEFAULT_BLOCK_COMMENT");
        eclipseToIdeaAttributes.put("bracket", "DEFAULT_BRACKETS");
        eclipseToIdeaAttributes.put("class", "DEFAULT_CLASS_NAME");
        eclipseToIdeaAttributes.put("constant", "DEFAULT_CONSTANT");
        eclipseToIdeaAttributes.put("javadoc", "DEFAULT_DOC_COMMENT");
        eclipseToIdeaAttributes.put("javadocKeyword", "DEFAULT_DOC_COMMENT_TAG");
        eclipseToIdeaAttributes.put("javadocTag", "DEFAULT_DOC_MARKUP");
        eclipseToIdeaAttributes.put("operator", "DEFAULT_DOT");
        eclipseToIdeaAttributes.put("keyword", "DEFAULT_ENTITY");
        eclipseToIdeaAttributes.put("method", "DEFAULT_FUNCTION_CALL");
        ECLIPSE_TO_IDEA_ATTRIBUTES = Collections.unmodifiableMap(eclipseToIdeaAttributes);

    }
    public static final Map<String, List<String>> ECLIPSE_TO_IDEA_OPTIONS;
    static
    {
        Map<String, List<String>> eclipseToIdeaOptions = new HashMap<>();
        eclipseToIdeaOptions.put("lineNumber", List.of("CARET_COLOR", "LINE_NUMBERS_COLOR"));
        eclipseToIdeaOptions.put("currentLine", Collections.singletonList("CARET_ROW_COLOR"));
        eclipseToIdeaOptions.put("background", List.of("CONSOLE_BACKGROUND_KEY", "GUTTER_BACKGROUND", "RIGHT_MARGIN_COLOR", "TEARLINE_COLOR"));
        eclipseToIdeaOptions.put("selectionBackground", List.of("SELECTION_BACKGROUND"));
        eclipseToIdeaOptions.put("selectionForeground", List.of("SELECTION_FOREGROUND"));

        ECLIPSE_TO_IDEA_OPTIONS = Collections.unmodifiableMap(eclipseToIdeaOptions);
    }
}
