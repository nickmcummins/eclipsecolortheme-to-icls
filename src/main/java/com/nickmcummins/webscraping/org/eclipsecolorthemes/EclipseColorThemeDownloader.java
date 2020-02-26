package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.idFromUrl;
import static com.nickmcummins.webscraping.Util.get;

public class EclipseColorThemeDownloader {
    private static final String START_PAGE = "https://web.archive.org/web/20190630000453/http://www.eclipsecolorthemes.org/?list=all&page=1";
    private static final String OUTPUT_DIR = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));
    private static final String THEME_INDEX = String.format("%s/themes.yaml", OUTPUT_DIR);
    private static final String PAGES_INDEX = String.format("%s/pages.yaml", OUTPUT_DIR);

    private static final Yaml YAML;
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(options);
    }
    private final String startPage;
    private final Map<Integer, String> downloadedThemesIndex;

    public EclipseColorThemeDownloader(String startPage) {
        this.startPage = startPage;
        this.downloadedThemesIndex = loadDownloadedThemesIndex();
    }

    private Map<Integer, String> loadDownloadedThemesIndex() {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(THEME_INDEX))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded theme index", e);
        }
    }

    private void writeDownloadedThemesIndex()
    {
        try (FileWriter file = new FileWriter(THEME_INDEX)) {
            StringWriter writer = new StringWriter();
            YAML.dump(downloadedThemesIndex, writer);
            file.write(writer.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to update downloaded theme index file", e);
        }
    }

    public void downloadThemesOnPage() throws InterruptedException, IOException
    {
        Document ectPage = Jsoup.parse(get(START_PAGE));
        List<String> themeUrls = ectPage.select("div[class='theme']").stream()
                .map(themeDiv -> String.format("https://web.archive.org%s", themeDiv.selectFirst("div[class='info']").selectFirst("a").attr("href")))
                .collect(Collectors.toList());

        for (String ectThemeUrl : themeUrls) {
            Integer themeId = Integer.valueOf(idFromUrl(ectThemeUrl));
            if (downloadedThemesIndex.containsKey(themeId)) {
                System.out.println(String.format("Skipping already downloaded theme %d", themeId));
            }
            else {
                EclipseColorTheme eclipseColorTheme;
                try {
                    eclipseColorTheme = EclipseColorTheme.fromWebpage(ectThemeUrl);
                } catch (IOException | InterruptedException e) {
                    writeDownloadedThemesIndex();
                    throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
                }
                String downloadedFilename = eclipseColorTheme.writeToFile(OUTPUT_DIR);
                downloadedThemesIndex.put(themeId, downloadedFilename.replace(String.format("%s/", OUTPUT_DIR), ""));
            }
        }

        writeDownloadedThemesIndex();

    }

    public static void main(String[] args) throws Exception
    {
        EclipseColorThemeDownloader ectDownloader = new EclipseColorThemeDownloader(START_PAGE);
        ectDownloader.downloadThemesOnPage();
    }
}
