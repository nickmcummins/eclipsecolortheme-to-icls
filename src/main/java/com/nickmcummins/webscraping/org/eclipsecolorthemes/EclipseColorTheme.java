package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.ThemeFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.net.http.HttpResponse.BodyHandlers;

public class EclipseColorTheme implements ThemeFile {
    private static final String EXTENSION = "xml";
    private final String id;
    private final String name;
    private final String author;
    private final String modified;
    private final Map<String, ColorThemeElement> settingsByName;

    public EclipseColorTheme(String id, String name, String author, String modified, Map<String, ColorThemeElement> settingsByName) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.modified = modified;
        this.settingsByName = settingsByName;
    }

    public static EclipseColorTheme fromWebpage(String url) {
        Document soup = Jsoup.parse(get(url));
        return new EclipseColorTheme(
                url.split("&")[url.split("&").length - 1].split("=")[1],
                soup.selectFirst("h2").select("b").text(),
                soup.selectFirst("h2").selectFirst("span").selectFirst("span").child(0).text(),
                null,
                soup.select("div[class='setting-entry']").stream()
                        .map(ColorThemeElement::fromHtmlPageDiv)
                        .collect(Collectors.toMap(entry -> entry.name, Function.identity())));
    }

    public static EclipseColorTheme fromXml(String filename) {
        try {
            Element colorTheme = Jsoup.parse(new String(Files.readAllBytes(Paths.get(filename))), "", Parser.xmlParser()).selectFirst("colorTheme");
            return new EclipseColorTheme(
                    colorTheme.attr("id"),
                    colorTheme.attr("name"),
                    colorTheme.attr("author"),
                    colorTheme.attr("modified"),
                    colorTheme.children().stream()
                            .collect(Collectors.toMap(Element::tagName, ColorThemeElement::fromXmlElement)));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Exception while trying to parse file %s", filename), e);
        }
    }

    private static String get(String url) {
        HttpClient requests = HttpClient.newBuilder().build();
        try {
            HttpResponse<String> response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ColorThemeElement> getSettingsByName()
    {
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

    public String getName()
    {
        return name;
    }

    public String getModified() {
        return modified;
    }

    @Override
    public String getExtension()
    {
        return EXTENSION;
    }
}
