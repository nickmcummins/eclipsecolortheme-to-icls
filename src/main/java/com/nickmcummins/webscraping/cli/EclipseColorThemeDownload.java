package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeDownloader;

import java.io.IOException;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeDownloader.START_PAGE;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "download", description = "Download from eclipsecolorthemes.org.")
public class EclipseColorThemeDownload implements Runnable {
    @Option(names = {"-r", "--refresh-page-list"}, description = "Refresh the page list index.")
    private boolean refreshPageList = false;

    @Option(names = {"-d", "--download"}, description = "Download themes.")
    private boolean downloadThemes = false;

    @Override
    public void run() {
        try {
            EclipseColorThemeDownloader downloader = new EclipseColorThemeDownloader(START_PAGE, refreshPageList, downloadThemes);
            downloader.downloadThemesOnPages();
        } catch (RuntimeException | InterruptedException | IOException | CannotDownloadException e) {
            e.printStackTrace();
        }
    }
}
