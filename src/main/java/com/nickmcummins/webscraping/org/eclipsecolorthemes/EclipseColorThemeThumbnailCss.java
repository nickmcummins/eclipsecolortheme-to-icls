package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class EclipseColorThemeThumbnailCss {
    private static final List<String> CSS_FIELDS = List.of(
            "number", "string", "operator", "keyword", "class", "method", "javadoc", "localvariabledeclaration",
            "parametervariable", "field", "staticfinalfield"
    );
    private static final String JAVA_CSS_FIELD = """  
            .java .%s {
                color: %s; 
            }      
            """;
    private static final String UNSET_COLOR = "#FFFFFF";
    private final Map<String, Element> colorTheme;

    public EclipseColorThemeThumbnailCss(String eclipseColorThemeXml) {
        try {
            this.colorTheme = Jsoup.parse(new String(Files.readAllBytes(Paths.get(eclipseColorThemeXml))), "", Parser.xmlParser())
                    .selectFirst("colorTheme").children().stream()
                    .collect(Collectors.toMap(element -> element.tagName().toLowerCase(), element -> element));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String javaCssFields() {
        return CSS_FIELDS.stream()
                .map(field -> String.format(JAVA_CSS_FIELD, field, fieldColor(field)))
                .collect(joining("\n\n"));
    }


    private String fieldColor(String fieldName) {
        if (colorTheme.containsKey(fieldName))
            return colorTheme.get(fieldName).attr("color");
        else
            return UNSET_COLOR;
    }

    public String toString() {
        return String.format("""
                .view {
                    background-color: %s;
                    width: 420px;
                }
                                
                .lineNumber {
                    color: %s;
                }
                                
                .sidebar {
                    border-right: 1px solid %s;
                }
                                
                .java .default {
                    color: %s;
                }
                
                %s
                                
                """, fieldColor("background"),
                fieldColor("linenumber"),
                fieldColor("linenumber"),
                fieldColor("foreground"),
                javaCssFields());
    }
}
