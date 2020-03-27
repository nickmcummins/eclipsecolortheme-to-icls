package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeDownloader;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "download", description = "Download from eclipsecolorthemes.org.")
public class EclipseColorThemeDownload implements Runnable {
    @Option(names = {"-s", "--start-id"})
    public String startId;

    @Option(names = {"-n", "--number-to-download"})
    public String numberToDownload;

    @Option(names = {"-l", "--use-last-id"})
    public boolean useLastId;

    @Override
    public void run() {
        try {
            EclipseColorThemeDownloader downloader = new EclipseColorThemeDownloader(startId, numberToDownload, useLastId);
            downloader.downloadThemes();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
