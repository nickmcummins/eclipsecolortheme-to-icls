package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

import java.io.IOException;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getLastDownloadedIndex;

public class ArchiveOrgEclipseColorThemeDownloader extends BaseEclipseColorThemeDownloader implements EclipseColorThemeDownloader {
    private static final String VIEW_THEME_URL = "http://web.archive.org/web/20191120230234/http://www.eclipsecolorthemes.org/?view=theme&id=%d";
    private static final int LATEST_ARCHIVED_THEME_ID = 59381;

    public ArchiveOrgEclipseColorThemeDownloader() {
        super();
    }

    public ArchiveOrgEclipseColorThemeDownloader(Integer startIndex, Integer numberToDownload, boolean generateThumbnails) {
        super(
                startIndex == null ? getLastDownloadedIndex() + 1 : startIndex,
                numberToDownload != null ? Math.max(((startIndex == null) ? getLastDownloadedIndex() + 1 : startIndex) + numberToDownload, LATEST_ARCHIVED_THEME_ID) : LATEST_ARCHIVED_THEME_ID,
                generateThumbnails);
    }

    @Override
    public EclipseColorTheme downloadTheme(int themeId, boolean writeToFile) throws CannotDownloadException {
        String ectThemeUrl = String.format(VIEW_THEME_URL, themeId);
        EclipseColorTheme eclipseColorTheme;
        try {
            print("Attempting to download %s", ectThemeUrl);
            eclipseColorTheme = EclipseColorTheme.fromWebpage(ectThemeUrl);
            if (writeToFile) eclipseColorTheme.writeToFile();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            throw new RuntimeException(String.format("\tException downloading theme %s", ectThemeUrl), e);
        }

        return eclipseColorTheme;
    }
}
