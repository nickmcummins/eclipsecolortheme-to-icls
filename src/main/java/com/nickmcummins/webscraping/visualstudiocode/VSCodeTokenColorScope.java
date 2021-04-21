package com.nickmcummins.webscraping.visualstudiocode;

import java.util.List;

public enum VSCodeTokenColorScope {
    COMMENT(List.of("comment", "punctuation.definition.comment", "string.comment"));

    private List<String> scope;

    VSCodeTokenColorScope(List<String> scope)
    {
        this.scope = scope;
    }
}
