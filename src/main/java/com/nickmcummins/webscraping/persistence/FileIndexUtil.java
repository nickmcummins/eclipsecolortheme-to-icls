package com.nickmcummins.webscraping.persistence;


import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.Util.print;

public class FileIndexUtil {
    public static final String ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));

    public static int getLastDownloadedIndex() {
        int lastDownloadedIndex = Arrays.stream(new File(ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY).list())
                .filter(FileIndexUtil::isNumericDirectory)
                .map(Integer::valueOf)
                .max(Comparator.naturalOrder()).orElse(0);
        print("Determined highest downloaded ID to be %d", lastDownloadedIndex);
        return lastDownloadedIndex;
    }

    public static List<String> getDownloadedXmls() {
        List<String> downloadedXmls = new ArrayList<>();

        List<String> downloadedDirs = Arrays.stream(new File(ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY).list())
                .filter(FileIndexUtil::isNumericDirectory)
                .collect(Collectors.toList());

        for (String downloadedDir : downloadedDirs) {
            downloadedXmls.addAll(Arrays.stream(new File(String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, downloadedDir)).list())
                    .filter(file -> file.endsWith(".xml"))
                    .map(file -> String.format("%s/%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, downloadedDir, file))
                    .collect(Collectors.toList()));
        }

        return downloadedXmls;
    }

    private static boolean isNumericDirectory(String filename) {
        File file = new File(String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, filename));
        return file.isDirectory() && NumberUtils.isDigits(filename);
    }
}
