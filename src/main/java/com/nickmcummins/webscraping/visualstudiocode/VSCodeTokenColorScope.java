package com.nickmcummins.webscraping.visualstudiocode;

import java.util.List;

public enum VSCodeTokenColorScope {
    COMMENT(List.of("comment", "punctuation.definition.comment", "string.comment")),
    BRACE(List.of("punctuation", "meta.tag.inline.any.html", "meta.tag.block.any.html", "meta.brace")),
    VARIABLE(List.of("constant", "entity.name.constant", "variable.other.constant", "variable.language", "meta.definition.variable")),
    ENTITY(List.of("entity", "entity.name")),
    PARAMETER("variable.parameter.function"),
    TAG("entity.name.tag"),
    FUNCTION("entity.name.function"),
    KEYWORD("keyword"),
    TYPE(List.of("storage", "storage.type")),
    STRING(List.of("string", "punctuation.definition.string", "string punctuation.section.embedded source"));


    private List<String> scope;

    VSCodeTokenColorScope(List<String> scope) {
        this.scope = scope;
    }

    VSCodeTokenColorScope(String scope) {
        this(List.of(scope));
    }
    }
