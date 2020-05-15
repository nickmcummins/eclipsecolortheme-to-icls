package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.http.CannotDownloadException;

import java.io.IOException;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getLastDownloadedIndex;

public class EclipseColorThemeDownloader {
    private static final String VIEW_THEME_URL = "https://web.archive.org/web/20190824073023/http://www.eclipsecolorthemes.org/?view=theme&id=%d";
    private static final int LATEST_ARCHIVED_THEME_ID = 58465;
    private final int startIndex;
    private final int endIndex;

    public EclipseColorThemeDownloader(String startIndex, String numberToDownload, boolean useLastIndexForStart) {
        this.startIndex = useLastIndexForStart || startIndex == null ? getLastDownloadedIndex() + 1 : Integer.parseInt(startIndex);
        this.endIndex = numberToDownload != null ? Math.max(this.startIndex + Integer.parseInt(numberToDownload), LATEST_ARCHIVED_THEME_ID) : LATEST_ARCHIVED_THEME_ID;
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
            throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
        }

        return eclipseColorTheme;
    }

    public void downloadThemes() {
        for (int themeId = startIndex; themeId < endIndex; themeId++) {
            try {
                downloadTheme(themeId, true);
            } catch (CannotDownloadException ce) {
                print("Exception downloading theme id %d, skipping.", themeId);
            }
        }
    }
}
