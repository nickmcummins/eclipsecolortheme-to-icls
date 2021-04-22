package com.nickmcummins.webscraping.visualstudiocode;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class VSCodeTokenColor {
    private String name;
    private VSCodeTokenColorScope scope;
    private Map<String, String> settings;

    public VSCodeTokenColor(VSCodeTokenColorScope scope, String foregroundColor) {
        this.name = scope.name().charAt(0) + scope.name().toLowerCase().substring((1));
        this.scope = scope;
        this.settings = Map.of("foreground", foregroundColor);
    }

    public String toString() {
        return String.format("""
                {
                    "name": "%s",
                    "scope": %s,
                    "settings": {
                        %s
                    }
                }
                """, name, scope, settings.entrySet().stream().map(s -> '"' + s.getKey() +'"' + ": " + '"' + s.getValue() + '"').collect(joining(",\n")));
    }
}
