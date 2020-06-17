package com.nickmcummins.webscraping.persistence;


import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileIndexUtil {
    public static final String ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));

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

    public static boolean isNumericDirectory(String filename) {
        File file = new File(String.format("%s/%s", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY, filename));
        return file.isDirectory() && NumberUtils.isDigits(filename);
    }

    public static boolean isNumberPrefixed(String filename) {
        if (filename.contains("-")) {
            String prefix = filename.split("-")[0];
            return NumberUtils.isDigits(prefix);
        } else {
            return false;
        }
    }
}
