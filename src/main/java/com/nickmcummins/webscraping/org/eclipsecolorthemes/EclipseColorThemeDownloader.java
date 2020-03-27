package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import java.io.IOException;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getLastDownloadedIndex;

public class EclipseColorThemeDownloader {
    private static final int LATEST_ARCHIVED_THEME_ID = 58465;
    private final int startIndex;
    private final int endIndex;

    public EclipseColorThemeDownloader(String startIndex, String numberToDownload, boolean useLastIndexForStart) {
        this.startIndex = useLastIndexForStart || startIndex == null ? getLastDownloadedIndex() + 1 : Integer.parseInt(startIndex);
        this.endIndex = numberToDownload != null ? Math.max(this.startIndex + Integer.parseInt(numberToDownload), LATEST_ARCHIVED_THEME_ID) : LATEST_ARCHIVED_THEME_ID;
    }

    public void downloadThemes() {
        for (int themeId = startIndex; themeId < endIndex; themeId++) {
            String ectThemeUrl = String.format("https://web.archive.org/web/20190824073023/http://www.eclipsecolorthemes.org/?view=theme&id=%d", themeId);
            EclipseColorTheme eclipseColorTheme;
            try {
                eclipseColorTheme = EclipseColorTheme.fromWebpage(ectThemeUrl);
                eclipseColorTheme.writeToFile();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(String.format("Exception downloading theme %s", ectThemeUrl), e);
            } catch (CannotDownloadException ce) {
                print("Exception downloading theme id %d, skipping.", themeId);
            }
        }
    }
}
