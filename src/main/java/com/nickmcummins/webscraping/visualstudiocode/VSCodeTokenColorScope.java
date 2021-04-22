package com.nickmcummins.webscraping.visualstudiocode;

import java.util.List;

import static java.util.stream.Collectors.joining;

public enum VSCodeTokenColorScope {
    COMMENT(List.of("comment", "punctuation.definition.comment", "string.comment")),
    BRACE(List.of("punctuation", "meta.tag.inline.any.html", "meta.tag.block.any.html", "meta.brace")),
    VARIABLE(List.of("constant", "entity.name.constant", "variable", "variable.other.constant", "variable.language", "meta.definition.variable")),
    ENTITY(List.of("entity", "entity.name")),
    PARAMETER("variable.parameter.function"),
    TAG("entity.name.tag"),
    FUNCTION(List.of("entity.name.function", "meta.require", "support.function.any-method", "meta.function-call", "meta.method-call", "variable.function")),
    KEYWORD(List.of("keyword", "keyword.operator.expression", "variable.language", "keyword.other.special-method.elixir", "meta.control.flow")),
    TYPE(List.of("storage", "storage.type")),
    STRING(List.of("string", "punctuation.definition.string", "string punctuation.section.embedded source")),
    PUNCTUATION(List.of("punctuation.comma.graphql", "punctuation.definition.variable", "punctuation.definition.parameters", "punctuation.definition.array", "punctuation.definition.function", "punctuation.brace", "punctuation.terminator.statement", "punctuation.delimiter.object.comma", "punctuation.definition.entity", "punctuation.definition", "punctuation.definition.string.begin.markdown", "punctuation.definition.string.end.markdown", "punctuation.separator.key-value", "punctuation.separator.dictionary", "punctuation.terminator", "punctuation.delimiter.comma", "punctuation.separator.comma", "punctuation.accessor", "punctuation.separator.array", "punctuation.section", "punctuation.section.property-list.begin.bracket.curly", "punctuation.section.property-list.end.bracket.curly", "punctuation.separator.statement", "punctuation.separator.parameter", "punctuation.section.array.elixir", "punctuation.separator.object.elixir", "punctuation.section.embedded.elixir", "punctuation.section.function.elixir", "punctuation.section.scope.elixir", "punctuation.section.embedded", "meta.brace.round", "meta.brace.square", "meta.brace.curly", "constant.name.attribute.tag.pug", "punctuation.section.embedded", "punctuation.separator.method", "punctuation.separator", "punctuation.other.comma", "punctuation.bracket", "keyword.control.ternary", "string.interpolated.pug", "support.function.interpolation.sass", "punctuation.parenthesis.begin", "punctuation.parenthesis.end", "punctuation.operation.graphql", "punctuation.colon.graphql")),
    OPERATORS("keyword.operator"),
    CLASSES(List.of("support.class", "entity.name.class", "entity.name.type.class", "meta.class.instance", "meta.class.inheritance", "entity.other.inherited-class", "entity.name.type", "variable.other.constant.elixir", "storage.type.haskell", "support.type.graphql", "support.type.enum.graphql")),
    METHODS("keyword.other.special-method"),
    NUMBERS(List.of("constant.numeric")),
    CONSTANTS(List.of("constant", "variable.other.constant", "constant.other.symbol", "constant.language.symbol", "support.constant", "support.variable.magic.python"));

    private List<String> scope;

    VSCodeTokenColorScope(List<String> scope) {
        this.scope = scope;
    }

    VSCodeTokenColorScope(String scope) {
        this(List.of(scope));
    }

    public String toString()
    {
        return String.format("""
                [
                    %s
                ]
                """, scope.stream().map(s -> "        " + '"' + s +'"').collect(joining(",\n")));
    }
}
