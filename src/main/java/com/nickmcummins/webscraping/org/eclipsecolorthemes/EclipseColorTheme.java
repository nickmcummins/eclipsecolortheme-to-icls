package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.ColorTheme;
import com.nickmcummins.webscraping.ColorUtil;
import com.nickmcummins.webscraping.SchemeType;
import com.nickmcummins.webscraping.http.CannotDownloadException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.ColorUtil.rgbString;
import static com.nickmcummins.webscraping.Util.*;
import static com.nickmcummins.webscraping.http.HttpUtil.get;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorSettingField.*;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;

public class EclipseColorTheme implements ColorTheme {
    private static final String URL_UNAVAILABLE = "No URL has been captured for this domain.";
    private static final String SITE_MAINTENANCE = "We’ll be back soon!";
    public static final String EXTENSION = "xml";
    private static final List<EclipseColorSettingField> SETTINGS_ORDER = List.of(searchResultIndication,
            filteredSearchResultIndication, occurrenceIndication, writeOccurrenceIndication, findScope,
            deletionIndication, sourceHoverBackground, singleLineComment, multiLineComment,
            commentTaskTag, javadoc, javadocLink, javadocTag, javadocKeyword, classColor, interfaceColor, method,
            methodDeclaration, bracket, number, stringColor, operator, keyword, annotation, staticMethod,
            localVariable, localVariableDeclaration, field, staticField, staticFinalField, deprecatedMember,
            enumColor, inheritedMethod, abstractMethod, parameterVariable, typeArgument, typeParameter, constant,
            background, currentLine, foreground, lineNumber, selectionBackground, selectionForeground);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String id;
    private final String name;
    private final String author;
    private final LocalDateTime modified;
    private final Map<EclipseColorSettingField, EclipseColorThemeElement> settingsByName;
    private final SchemeType lightOrDark;
    private String filename;

    public EclipseColorTheme(String id, String name, String author, String modified, Map<EclipseColorSettingField, EclipseColorThemeElement> settingsByName) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.modified = parseDate(DATE_FORMAT, modified);
        this.settingsByName = settingsByName;

        Color backgroundColor = Color.decode(settingsByName.get(background).getColorValue());
        Color textColor = Color.decode(settingsByName.get(foreground).getColorValue());
        this.lightOrDark = ColorUtil.isDark(backgroundColor) && ColorUtil.isLight(textColor) ? DARK : LIGHT;
    }

    static EclipseColorTheme fromWebpage(String url) throws InterruptedException, CannotDownloadException {
        String webpage = get(url);
        if (webpage.contains(URL_UNAVAILABLE) || webpage.contains(SITE_MAINTENANCE)) {
            throw new CannotDownloadException();
        }
        try {
            Document soup = Jsoup.parse(webpage);
            return new EclipseColorTheme(
                    idFromUrl(url),
                    soup.selectFirst("h2").select("b").text(),
                    soup.selectFirst("h2").selectFirst("span").selectFirst("span").child(0).text(),
                    DATE_FORMAT.format(LocalDateTime.now()),
                    soup.select("div[class='setting-entry']").stream()
                            .map(EclipseColorThemeElement::fromHtmlPageDiv)
                            .collect(Collectors.toMap(EclipseColorSettingField::fromColorThemeElement, Function.identity())));
        } catch (Exception e) {
            print(webpage);
            throw new CannotDownloadException(e);
        }
    }

    private static String idFromUrl(String url)
    {
        return url.split("&")[url.split("&").length - 1].split("=")[1];
    }

    public static EclipseColorTheme fromString(String string) {
        Element colorTheme = Jsoup.parse(string, "", Parser.xmlParser()).selectFirst("colorTheme");
        return new EclipseColorTheme(
                colorTheme.attr("id"),
                colorTheme.attr("name"),
                colorTheme.attr("author"),
                colorTheme.attr("modified"),
                colorTheme.children().stream()
                        .collect(Collectors.toMap(EclipseColorSettingField::fromXmlElement, EclipseColorThemeElement::fromXmlElement)));
    }

    public static EclipseColorTheme fromXmlFile(String filename) {
        try {
            return fromString(new String(Files.readAllBytes(Paths.get(filename))));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Exception while trying to read file %s", filename), e);
        }
    }

    public SchemeType getLightOrDark() {
        return lightOrDark;
    }

    public Map<EclipseColorSettingField, EclipseColorThemeElement> getSettingsByName() {
        return settingsByName;
    }

    public EclipseColorThemeElement getSettingByName(EclipseColorSettingField settingName) {
        if (settingsByName.containsKey(settingName)) {
            return settingsByName.get(settingName);
        }

        return null;
    }

    String toColorsYaml() {
        Map<String, Map<String, String>> colors = new HashMap<>();
        for (EclipseColorThemeElement colorThemeElement : settingsByName.values()) {
            colors.put(colorThemeElement.name, Map.of(
                    "hex", colorThemeElement.getColorValue(),
                    "rgb", rgbString(Color.decode(colorThemeElement.getColorValue())))
            );
        }
        Map<String, Map<String, Map<String, String>>> themeColors = Map.of(name, colors);

        return YAML.dump(themeColors);
    }

    public String toString() {
        String settingsTags = settingsByName.entrySet().stream()
                .sorted(Comparator.comparing(setting -> SETTINGS_ORDER.indexOf(setting.getKey())))
                .map(setting -> String.format("    %s", setting.getValue()))
                .collect(Collectors.joining("\n"));
        return String.format("""
                <?xml version="1.0" encoding="utf-8"?>
                <colorTheme id="%s" name="%s" author="%s">
                %s
                </colorTheme>""", id, name, author, settingsTags);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public void writeToFile() throws IOException {
        String outputDir = String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, id);
        Files.createDirectory(Paths.get(outputDir));
        this.filename = ColorTheme.super.writeToFile(outputDir);
    }
}
