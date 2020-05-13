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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.http.HttpUtil.get;
import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.SchemeType.DARK;
import static com.nickmcummins.webscraping.SchemeType.LIGHT;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;

public class EclipseColorTheme implements ColorTheme {
    private static final String URL_UNAVAILABLE = "No URL has been captured for this domain.";
    private static final String SITE_MAINTENANCE = "We’ll be back soon!";
    private static final String EXTENSION = "xml";
    private final String id;
    private final String name;
    private final String author;
    private String modified;
    private final Map<String, ColorThemeElement> settingsByName;

    public EclipseColorTheme(String id, String name, String author, String modified, Map<String, ColorThemeElement> settingsByName) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.modified = modified;
        this.settingsByName = settingsByName;
    }

    public static EclipseColorTheme fromWebpage(String url) throws InterruptedException, CannotDownloadException {
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
                    null,
                    soup.select("div[class='setting-entry']").stream()
                            .map(ColorThemeElement::fromHtmlPageDiv)
                            .collect(Collectors.toMap(entry -> entry.name, Function.identity())));
        } catch (Exception e) {
            print(webpage);
            throw new CannotDownloadException(e);
        }
    }

    public static String idFromUrl(String url)
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
                        .collect(Collectors.toMap(Element::tagName, ColorThemeElement::fromXmlElement)));
    }

    public static EclipseColorTheme fromXmlFile(String filename) {
        try {
            return fromString(new String(Files.readAllBytes(Paths.get(filename))));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Exception while trying to read file %s", filename), e);
        }
    }

    public SchemeType getLightOrDark()
    {
        Color backgroundColor = Color.decode(settingsByName.get("background").getColorValue());
        Color textColor = Color.decode(settingsByName.get("foreground").getColorValue());
        print("Determined %s to be dark.", name);
        return ColorUtil.isDark(backgroundColor) && ColorUtil.isLight(textColor) ? DARK : LIGHT;
    }

    public Map<String, ColorThemeElement> getSettingsByName() {
        return settingsByName;
    }

    public String toString() {
        String settingsTags = settingsByName.values().stream()
                .map(colorThemeElement -> String.format("\t%s", colorThemeElement))
                .collect(Collectors.joining("\n"));
        return String.format("""
                <?xml version="1.0" encoding="utf-8"?>
                <colorTheme id="%s" name="%s" author="%s">
                %s
                </colorTheme>
                    """, id, name, author, settingsTags);
    }

    public String getName() {
        return name;
    }

    public String getModified() {
        return modified;
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public void writeToFile() throws IOException {
        String outputDir = String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, id);
        Files.createDirectory(Paths.get(outputDir));
        ColorTheme.super.writeToFile(outputDir);
    }
}
