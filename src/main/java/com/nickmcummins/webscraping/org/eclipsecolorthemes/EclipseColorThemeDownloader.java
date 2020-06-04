package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.http.CannotDownloadException;

import java.io.IOException;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.cli.EclipseColorThemeGenerateThumbnailCommand.THUMBNAILS_DIRECTORY;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getLastDownloadedIndex;

public class EclipseColorThemeDownloader {
    private static final String VIEW_THEME_URL = "http://web.archive.org/web/20191120230234/http://www.eclipsecolorthemes.org/?view=theme&id=%d";
    private static final int LATEST_ARCHIVED_THEME_ID = 59381;
    private final int startIndex;
    private final int endIndex;
    private final boolean generateThumbnails;

    public EclipseColorThemeDownloader(String startIndex, String numberToDownload, boolean generateThumbnails) {
        this.startIndex = startIndex == null ? getLastDownloadedIndex() + 1 : Integer.parseInt(startIndex);
        this.endIndex = numberToDownload != null ? Math.max(this.startIndex + Integer.parseInt(numberToDownload), LATEST_ARCHIVED_THEME_ID) : LATEST_ARCHIVED_THEME_ID;
        this.generateThumbnails = generateThumbnails;
    }

    public static EclipseColorTheme downloadTheme(int themeId, boolean writeToFile) throws CannotDownloadException {
        String ectThemeUrl = String.format(VIEW_THEME_URL, themeId);
        EclipseColorTheme eclipseColorTheme;
        try {
            print("Attempting to download %s", ectThemeUrl);
            eclipseColorTheme = EclipseColorTheme.fromWebpage(ectThemeUrl);
            if (writeToFile)
                eclipseColorTheme.writeToFile();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            throw new RuntimeException(String.format("\tException downloading theme %s", ectThemeUrl), e);
        }

        return eclipseColorTheme;
    }

    public void downloadThemes() {
        for (int themeId = startIndex; themeId < endIndex; themeId++) {
            try {
                EclipseColorTheme eclipseColorTheme = downloadTheme(themeId, true);
                if (generateThumbnails)
                    new EclipseColorThemeThumbnail(eclipseColorTheme.getFilename(), THUMBNAILS_DIRECTORY).generate();
            } catch (CannotDownloadException ce) {
                print("\tException downloading theme id %d, skipping.", themeId);
            }
        }
    }
}
