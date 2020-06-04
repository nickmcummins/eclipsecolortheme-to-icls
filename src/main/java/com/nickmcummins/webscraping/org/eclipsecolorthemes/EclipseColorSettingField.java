package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EclipseColorSettingField {
    selectionBackground,
    typeParameter,
    methodDeclaration,
    multiLineComment,
    constant,
    stringColor("string"),
    searchResultIndication,
    typeArgument,
    singleLineComment,
    foreground,
    interfaceColor("interface"),
    operator,
    staticField,
    javadocKeyword,
    javadoc,
    number,
    staticFinalField,
    localVariable,
    deletionIndication,
    bracket,
    deprecatedMember,
    keyword,
    classColor("class"),
    inheritedMethod,
    currentLine,
    annotation,
    writeOccurrenceIndication,
    javadocLink,
    selectionForeground,
    method,
    findScope,
    javadocTag,
    parameterVariable,
    enumColor("enum"),
    commentTaskTag,
    localVariableDeclaration,
    field,
    background,
    occurrenceIndication,
    abstractMethod,
    lineNumber,
    staticMethod,
    filteredSearchResultIndication,
    sourceHoverBackground;

    public static final Map<String, EclipseColorSettingField> STRING_TO_SETTINGS = Arrays.stream(EclipseColorSettingField.values())
            .collect(Collectors.toMap(settingField -> settingField.name != null ? settingField.name : settingField.toString(), settingField -> settingField));

    private final String name;

    EclipseColorSettingField() {
        this(null);
    }

    EclipseColorSettingField(String name) {
        this.name = name;
    }

    public static EclipseColorSettingField fromString(String settingString) {
        return STRING_TO_SETTINGS.get(settingString);
    }

    public static EclipseColorSettingField fromColorThemeElement(EclipseColorThemeElement colorThemeElement) {
        return fromString(colorThemeElement.name);
    }

    public static EclipseColorSettingField fromXmlElement(Element xmlElement) {
        return fromString(xmlElement.tagName());
    }
}