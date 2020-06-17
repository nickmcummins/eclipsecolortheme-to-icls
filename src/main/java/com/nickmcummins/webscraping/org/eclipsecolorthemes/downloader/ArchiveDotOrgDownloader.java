package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeSettingElement;
import com.nickmcummins.webscraping.persistence.FileIndexUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.http.HttpUtil.get;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme.DATE_FORMAT;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;

public class ArchiveDotOrgDownloader extends BaseEclipseColorThemeDownloader implements EclipseColorThemeDownloader {
    private static final String VIEW_THEME_URL = "http://web.archive.org/web/20191120230234/http://www.eclipsecolorthemes.org/?view=theme&id=%d";
    private static final int LATEST_ARCHIVED_THEME_ID = 59381;
    private static final String URL_UNAVAILABLE = "No URL has been captured for this domain.";
    private static final String SITE_MAINTENANCE = "We’ll be back soon!";

    ArchiveDotOrgDownloader() {
        super();
    }

    public ArchiveDotOrgDownloader(Integer startIndex, Integer endIndex, Integer maxDownloads, Boolean writeToFile, Boolean generateThumbnails) {
        super(startIndex, endIndex, maxDownloads, writeToFile, generateThumbnails, ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY);
    }

    @Override
    public EclipseColorTheme downloadTheme(int themeId) throws CannotDownloadException, InterruptedException {
        String ectThemeUrl = String.format(VIEW_THEME_URL, themeId);
        EclipseColorTheme eclipseColorTheme;

        print("Attempting to download %s", ectThemeUrl);
        eclipseColorTheme = downloadFromWebpage(themeId, ectThemeUrl);

        return eclipseColorTheme;
    }

    @Override
    public int getLastDownloadedIndex() {
        int lastDownloadedIndex = Arrays.stream(new File(downloadDirectory).list())
                .filter(FileIndexUtil::isNumericDirectory)
                .map(Integer::valueOf)
                .max(Comparator.naturalOrder()).orElse(0);
        print("Determined highest downloaded ID to be %d", lastDownloadedIndex);
        return lastDownloadedIndex;
    }
    
    @Override
    public int getIdMax() {
        return LATEST_ARCHIVED_THEME_ID;
    }

    private static EclipseColorTheme downloadFromWebpage(int themeId, String url) throws InterruptedException, CannotDownloadException {
        String webpage = get(url);
        if (webpage.contains(URL_UNAVAILABLE) || webpage.contains(SITE_MAINTENANCE)) {
            throw new CannotDownloadException();
        }
        try {
            Document soup = Jsoup.parse(webpage);
            return new EclipseColorTheme(
                    themeId,
                    soup.selectFirst("h2").select("b").text(),
                    soup.selectFirst("h2").selectFirst("span").selectFirst("span").child(0).text(),
                    DATE_FORMAT.format(LocalDateTime.now()),
                    soup.select("div[class='setting-entry']").stream()
                            .map(EclipseColorThemeSettingElement::fromHtmlPageDiv)
                            .collect(Collectors.toMap(EclipseColorThemeSettingElement.Name::fromColorThemeElement, Function.identity())));
        } catch (Exception e) {
            print(webpage);
            throw new CannotDownloadException(e);
        }
    }

}
