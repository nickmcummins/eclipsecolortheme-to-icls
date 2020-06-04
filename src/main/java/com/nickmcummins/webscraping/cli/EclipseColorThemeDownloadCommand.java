package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeDownloader;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "download", description = "Download from eclipsecolorthemes.org.")
public class EclipseColorThemeDownloadCommand implements Runnable {
    @Option(names = {"-s", "--start-id"})
    public String startId;

    @Option(names = {"-n", "--number-to-download"})
    public String numberToDownload;

    @Option(names = {"-t", "--generate-thumbnails"})
    public boolean generateThumbnails;

    @Override
    public void run() {
        try {
            EclipseColorThemeDownloader downloader = new EclipseColorThemeDownloader(startId, numberToDownload, generateThumbnails);
            downloader.downloadThemes();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
