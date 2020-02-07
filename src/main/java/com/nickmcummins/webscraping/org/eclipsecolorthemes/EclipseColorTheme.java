package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.net.http.HttpResponse.BodyHandlers;


public class EclipseColorTheme
{
    private String id;
    private String name;
    private String author;
    private Document soup;
    private Map<String, SettingEntry> settingsByName;

    public EclipseColorTheme(String url)
    {
        this.id = url.split("&")[url.split("&").length - 1].split("=")[1];
        this.soup = Jsoup.parse(get(url));
        this.name = this.soup.selectFirst("h2").select("b").text();
        this.author = this.soup.selectFirst("h2").selectFirst("span").selectFirst("span").child(0).text();
        this.settingsByName = this.soup.select("div[class='setting-entry']").stream()
                .map(SettingEntry::new)
                .collect(Collectors.toMap(entry -> entry.name, Function.identity()));

    }

    private static String get(String url)
    {
        HttpClient requests = HttpClient.newBuilder().build();
        try
        {
            HttpResponse<String> response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), BodyHandlers.ofString());
            return response.body();
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String toString()
    {
        String settingsTags = settingsByName.values().stream()
                .map(settingEntry -> String.format("\t%s", settingEntry))
                .collect(Collectors.joining("\n"));
        return String.format("""
            <?xml version="1.0" encoding="utf-8"?>
            <colorTheme id="%s" name="%s" author="%s">
            %s
            </colorTheme>
                """, id, name, author, settingsTags);
    }

    public void writeToFile()
    {
        try (FileWriter file = new FileWriter(String.format("%s.xml", name.replaceAll(" ", "-"))))
        {
            file.write(toString());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
