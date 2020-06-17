package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.persistence.FileIndexUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.http.HttpUtil.get;

public class EclipseColorThemeDotOrgDownloader extends BaseEclipseColorThemeDownloader implements EclipseColorThemeDownloader {
    private static final String DOWNLOAD_THEME_URL = "http://www.eclipsecolorthemes.org/?view=empty&action=download&theme=%d&type=xml";

    public EclipseColorThemeDotOrgDownloader(Integer startIndex, Integer endIndex, Integer maxDownloads, Boolean writeToFile, Boolean generateThumbnails, String downloadDirectory) {
        super(startIndex, endIndex, maxDownloads, writeToFile, generateThumbnails, downloadDirectory);
    }

    public EclipseColorThemeDotOrgDownloader() {
    }

    @Override
    public EclipseColorTheme downloadTheme(int themeId) throws CannotDownloadException, InterruptedException {
        String eclipseColorThemeXml = downloadThemeXml(themeId);
        return EclipseColorTheme.fromString(themeId, eclipseColorThemeXml);
    }

    @Override
    public int getLastDownloadedIndex() {
        int lastDownloadedIndex = Arrays.stream(new File(downloadDirectory).list())
                .filter(FileIndexUtil::isNumberPrefixed)
                .map(file -> Integer.valueOf(file.split("-")[0]))
                .max(Comparator.naturalOrder()).orElse(0);
        print("Determined highest downloaded ID to be %d", lastDownloadedIndex);
        return lastDownloadedIndex;
    }

    @Override
    public int getIdMax() {
        return 60000;
    }

    public String downloadThemeXml(int themeId) throws CannotDownloadException, InterruptedException {
        String downloadThemeUrl = String.format(DOWNLOAD_THEME_URL, themeId);
        print("Attempting to download %s", downloadThemeUrl);
        return get(downloadThemeUrl);
    }
}
