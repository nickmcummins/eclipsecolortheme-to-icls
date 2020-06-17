package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader.ArchiveDotOrgDownloader;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader.BaseEclipseColorThemeDownloader;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader.EclipseColorThemeDotOrgDownloader;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader.EclipseColorThemeDownloader;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.jsoup.internal.StringUtil.isBlank;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "download", description = "Download from eclipsecolorthemes.org.")
public class EclipseColorThemeDownloadCommand implements Runnable {
    enum DownloaderType {
        ARCHIVE_DOT_ORG(ArchiveDotOrgDownloader.class, "archive.org"),
        ECLIPSE_COLOR_THEMES_DOR_ORG(EclipseColorThemeDotOrgDownloader.class, "eclipsecolorthemes.org");

        static final Map<String, DownloaderType> BY_DISPLAY_URL = Arrays.stream(DownloaderType.values())
                .collect(toMap(downloaderType -> downloaderType.displayUrl, downloaderType -> downloaderType));

        private final Class<? extends BaseEclipseColorThemeDownloader> downloaderClass;
        private final String displayUrl;

        DownloaderType(Class<? extends BaseEclipseColorThemeDownloader> downloaderClass, String displayUrl) {
            this.downloaderClass = downloaderClass;
            this.displayUrl = displayUrl;
        }

        public static DownloaderType fromDisplayUrl(String displayUrl) {
            return BY_DISPLAY_URL.get(displayUrl);
        }

        public EclipseColorThemeDownloader initializeDownloader(Integer startId, Integer endId, Integer maxDownloads, Boolean writeToFile, Boolean generateThumbnails, String downloadDirectory) throws Exception {
                return (EclipseColorThemeDownloader)(isBlank(downloadDirectory)
                        ? downloaderClass.getConstructor(Integer.class, Integer.class, Integer.class, Boolean.class, Boolean.class).newInstance(startId, endId, maxDownloads, writeToFile, generateThumbnails)
                        : downloaderClass.getConstructor(Integer.class, Integer.class, Integer.class, Boolean.class, Boolean.class, String.class).newInstance(startId, endId, maxDownloads, writeToFile, generateThumbnails, downloadDirectory));
        }
    }

    @Option(names = {"-s", "--start-id"})
    private Integer startId;

    @Option(names = {"-e", "--end-id"})
    private Integer endId;

    @Option(names = {"-n", "--number-to-download"})
    private Integer numberToDownload;

    @Option(names = {"-u", "--url"}, description = "archive.org,eclipsecolorthemes.org", required = true)
    private String downloaderType = null;

    @Option(names = {"-t", "--generate-thumbnails"})
    private boolean generateThumbnails;

    @Option(names = {"-d", "--download-directory"})
    private String downloadDirectory;

    @Override
    public void run() {
        try {
            EclipseColorThemeDownloader downloader = DownloaderType.fromDisplayUrl(downloaderType)
                    .initializeDownloader(startId, endId, numberToDownload, true, generateThumbnails, downloadDirectory);
            downloader.downloadThemes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
