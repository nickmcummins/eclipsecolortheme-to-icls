package com.nickmcummins.webscraping.persistence;


import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import static com.nickmcummins.webscraping.Util.print;

public class FileIndexUtil {
    public static final String ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));

    public static int getLastDownloadedIndex() {
        int lastDownloadedIndex = Arrays.stream(new File(ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY).list())
                .filter(file -> new File(String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, file)).isDirectory())
                .map(Integer::valueOf)
                .max(Comparator.naturalOrder()).orElse(0);
        print("Determined highest downloaded ID to be %d", lastDownloadedIndex);
        return lastDownloadedIndex;
    }
}
