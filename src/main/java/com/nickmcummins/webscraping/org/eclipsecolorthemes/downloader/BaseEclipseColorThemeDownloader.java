package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeThumbnail;

import java.nio.file.Paths;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;

public abstract class BaseEclipseColorThemeDownloader {
    private final int startIndex;
    private final Integer endIndex;
    private final int maxDownloads;
    private final boolean writeToFile;
    private final boolean generateThumbnails;
    protected final String downloadDirectory;

    public BaseEclipseColorThemeDownloader(Integer startIndex, Integer endIndex, Integer maxDownloads, Boolean writeToFile, Boolean generateThumbnails, String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;

        this.startIndex = startIndex != null ? getLastDownloadedIndex() + 1 : startIndex;
        this.endIndex = endIndex;
        this.maxDownloads = maxDownloads != null ? Math.min((startIndex == null ? getLastDownloadedIndex() + 1 : startIndex) + maxDownloads, getIdMax()) : getIdMax();
        this.writeToFile = writeToFile;
        this.generateThumbnails = generateThumbnails;
    }

    public BaseEclipseColorThemeDownloader() {
        this(-1, -1, -1, false, false, ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY);
    }

    public abstract EclipseColorTheme downloadTheme(int themeId) throws CannotDownloadException, InterruptedException;

    public void downloadThemes() {
        int endIndex = this.endIndex != null ? Math.min(this.endIndex, startIndex + maxDownloads) : Math.min(getIdMax(), startIndex + maxDownloads);
        for (int themeId = startIndex; themeId < endIndex; themeId++) {
            try {
                EclipseColorTheme eclipseColorTheme = downloadTheme(themeId);
                if (writeToFile) {
                    eclipseColorTheme.writeToFile(Paths.get(downloadDirectory));
                    if (generateThumbnails) new EclipseColorThemeThumbnail(eclipseColorTheme.getFilename(), true).generate(getThumbnailsDirectory());
                }
            } catch (CannotDownloadException | NullPointerException | InterruptedException ce) {
                print("\tException downloading theme id %d, skipping.", themeId);
            }
        }
    }

    public String getThumbnailsDirectory() {
        return String.format("%s/thumbnails", downloadDirectory);
    }

    public abstract int getLastDownloadedIndex();

    public abstract int getIdMax();
}
