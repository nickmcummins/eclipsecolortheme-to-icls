package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.persistence.yaml.YamlFilePersistence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ConnectException;
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
    private final List<String> downloadedPagesIndex;

    public EclipseColorThemeDownloader(String startPage) {
        this.startPage = startPage;
        this.yamlFilePersistence = new YamlFilePersistence();
        this.downloadedThemesIndex = yamlFilePersistence.getDownloadedThemesIndex();
        this.downloadedPagesIndex = yamlFilePersistence.getDownloadedPagesIndex();
    }

    public void downloadThemesOnPages(List<String> pages) throws InterruptedException, IOException
    {
        for (String page : pages) {
            if (page.contains("page=") && !downloadedPagesIndex.contains(page)) {
                Document ectPage = Jsoup.parse(get(String.format("https://web.archive.org/%s", page)));
                List<String> themeUrls = ectPage.select("div[class='theme']").stream()
                        .map(themeDiv -> String.format("https://web.archive.org%s", themeDiv.selectFirst("div[class='info']").selectFirst("a").attr("href")))
                        .collect(Collectors.toList());

                for (String ectThemeUrl : themeUrls) {
                    Integer themeId = Integer.valueOf(idFromUrl(ectThemeUrl));
                    if (downloadedThemesIndex.containsKey(themeId)) {
                        System.out.println(String.format("Skipping already downloaded theme %d", themeId));
                    } else {
                        EclipseColorTheme eclipseColorTheme;
                        try {
                            eclipseColorTheme = EclipseColorTheme.fromWebpage(ectThemeUrl);
                        } catch (IOException | InterruptedException e) {
                            yamlFilePersistence.writeIndexes();
                            throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
                        }
                        String downloadedFilename = eclipseColorTheme.writeToFile(OUTPUT_DIR);
                        downloadedThemesIndex.put(themeId, downloadedFilename.replace(String.format("%s/", OUTPUT_DIR), ""));
                    }
                }

                downloadedPagesIndex.add(page);
            }
            else
            {
                System.out.println("Skipping already downloaded page " + page);
            }
        }
        yamlFilePersistence.writeIndexes();
    }

    private List<String> updatePageIndex() throws IOException, InterruptedException {
        Document ectPage = Jsoup.parse(get(START_PAGE));
        List<String> pageUrls = ectPage.select("div[class='selector'] li a").stream()
                .map(a -> a.attr("href"))
                .filter(url -> url.contains("page="))
                .collect(Collectors.toList());
        return pageUrls;

    }

    public static void main(String[] args) throws Exception
    {
        EclipseColorThemeDownloader ectDownloader = new EclipseColorThemeDownloader(START_PAGE);
        List<String> pageUrls = ectDownloader.updatePageIndex();
        do {
            try {
                System.out.println(String.format("%d of %d pages downloaded.", ectDownloader.downloadedPagesIndex.size(), pageUrls.size()));
                ectDownloader.downloadThemesOnPages(pageUrls);
            } catch (RuntimeException re) {
                System.out.println("Connection reset");
            }
        } while (pageUrls.size() > ectDownloader.downloadedPagesIndex.size());

    }
}
