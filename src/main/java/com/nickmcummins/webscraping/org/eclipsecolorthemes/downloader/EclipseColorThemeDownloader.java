package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

public interface EclipseColorThemeDownloader {
    EclipseColorTheme downloadTheme(int themeId) throws CannotDownloadException, InterruptedException;

    void downloadThemes();
}
