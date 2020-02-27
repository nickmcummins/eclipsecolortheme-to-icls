package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.persistence.yaml.YamlFilePersistence;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.idFromUrl;
import static com.nickmcummins.webscraping.Util.get;
import static com.nickmcummins.webscraping.persistence.yaml.YamlFilePersistence.OUTPUT_DIR;

public class EclipseColorThemeDownloader {
    private static final String START_PAGE = "https://web.archive.org/web/20190630000453/http://www.eclipsecolorthemes.org/?list=all&page=1";

    private final String startPage;
    private final YamlFilePersistence yamlFilePersistence;
    private final Map<Integer, String> downloadedThemesIndex;
    private final Map<String, Map<Integer, List<String>>> downloadedPagesIndex;

    public EclipseColorThemeDownloader(String startPage) {
        this.startPage = startPage;
        this.yamlFilePersistence = new YamlFilePersistence();
        this.downloadedThemesIndex = yamlFilePersistence.getDownloadedThemesIndex();
        this.downloadedPagesIndex = yamlFilePersistence.getDownloadedPagesIndex();
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
                    yamlFilePersistence.writeDownloadedThemesIndex();
                    throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
                }
                String downloadedFilename = eclipseColorTheme.writeToFile(OUTPUT_DIR);
                downloadedThemesIndex.put(themeId, downloadedFilename.replace(String.format("%s/", OUTPUT_DIR), ""));
            }
        }

        yamlFilePersistence.writeDownloadedThemesIndex();
    }

    private void updatePageIndex() throws IOException, InterruptedException {
        Document ectPage = Jsoup.parse(get(START_PAGE));
        List<String> pageUrls = ectPage.select("div[class='selector'] li a").stream()
                .map(a -> a.attr("href"))
                .collect(Collectors.toList());
        System.out.println(StringUtil.join(pageUrls, "\n"));
    }

    public static void main(String[] args) throws Exception
    {
        EclipseColorThemeDownloader ectDownloader = new EclipseColorThemeDownloader(START_PAGE);
        //ectDownloader.downloadThemesOnPage();
        ectDownloader.updatePageIndex();
    }
}
