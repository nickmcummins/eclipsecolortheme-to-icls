package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeThumbnail;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.cli.EclipseColorThemeGenerateThumbnailCommand.THUMBNAILS_DIRECTORY;

public abstract class BaseEclipseColorThemeDownloader {
    private final int startIndex;
    private final int endIndex;
    private final boolean generateThumbnails;

    public BaseEclipseColorThemeDownloader(int startIndex, int endIndex, boolean generateThumbnails) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.generateThumbnails = generateThumbnails;
    }


    public BaseEclipseColorThemeDownloader() {
        this(-1, -1, false);
    }

    public abstract EclipseColorTheme downloadTheme(int themeId, boolean writeToFile) throws CannotDownloadException;

    public EclipseColorTheme downloadTheme(int themeId) throws CannotDownloadException {
        return downloadTheme(themeId, false);
    }

    public void downloadThemes() {
        for (int themeId = startIndex; themeId < endIndex; themeId++) {
            try {
                EclipseColorTheme eclipseColorTheme = downloadTheme(themeId, true);
                if (generateThumbnails) new EclipseColorThemeThumbnail(eclipseColorTheme.getFilename(), THUMBNAILS_DIRECTORY).generate();
            } catch (CannotDownloadException ce) {
                print("\tException downloading theme id %d, skipping.", themeId);
            }
        }
    }
}
