package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.persistence.yaml.YamlFilePersistence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.idFromUrl;
import static com.nickmcummins.webscraping.Util.get;
import static com.nickmcummins.webscraping.persistence.yaml.YamlFilePersistence.OUTPUT_DIR;

public class EclipseColorThemeDownloader {
    public static final String START_PAGE = "https://web.archive.org/web/20190630000453/http://www.eclipsecolorthemes.org/?list=all&page=1";

    private final String startPage;
    private final boolean refreshPageList;
    private final boolean downloadedThemes;
    private final YamlFilePersistence yamlFilePersistence;
    private final Map<Integer, String> downloadedThemesIndex;
    private final List<String> downloadedPagesIndex;

    public EclipseColorThemeDownloader(String startPage, boolean refreshPageList, boolean downloadThemes) throws IOException {
        this.startPage = startPage;
        this.refreshPageList = refreshPageList;
        this.downloadedThemes = downloadThemes;
        this.yamlFilePersistence = new YamlFilePersistence();
        this.downloadedThemesIndex = yamlFilePersistence.getDownloadedThemes();
        this.downloadedPagesIndex = yamlFilePersistence.getDownloadedPages();
    }

    public void downloadThemesOnPages() throws InterruptedException, IOException, CannotDownloadException {
        if (refreshPageList)
            yamlFilePersistence.refreshPageList(startPage);
        if (!downloadedThemes)
            return;

        for (String page : yamlFilePersistence.getPagesList()) {
            if (!downloadedPagesIndex.contains(page)) {
                try {
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
                                String downloadedFilename = eclipseColorTheme.writeToFile(OUTPUT_DIR);
                                downloadedThemesIndex.put(themeId, downloadedFilename.replace(String.format("%s/", OUTPUT_DIR), ""));
                            } catch (IOException | InterruptedException e) {
                                yamlFilePersistence.writePageListIndex();
                                throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
                            } catch (CannotDownloadException ignored) {
                            }

                        }
                    }

                    downloadedPagesIndex.add(page);
                } catch (CannotDownloadException cdte) {
                }
            } else {
                System.out.println("Skipping already downloaded page " + page);
            }
        }
        yamlFilePersistence.writePageListIndex();
    }
}
