package com.nickmcummins.webscraping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Util {
    public static void print(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    public static void print(Object object) {
        System.out.println(object);
    }

    public static String formatDate(SimpleDateFormat dateFormat, String dateString) {
        String formattedDate = dateFormat.format(System.currentTimeMillis());
        if (isNotBlank(dateString)) {
            try {
                formattedDate = dateFormat.format(dateString);
            } catch (Exception e) {
                print("Exception attempting to parse %s as a date string: %s\nUsing current datetime of %s as date value.", dateString, e.getMessage(), formattedDate);
            }
        }
        return formattedDate;
    }

    public static List<String> listFilesInDirectory(String directory, String extension) {
        return Arrays.stream(new File(directory).list())
                .filter(file -> file.contains(extension))
                .map(file -> String.format("%s/%s", directory, file))
                .collect(Collectors.toList());
    }


    public static String getResourceAsString(String filename) {
        return new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(String.format("/%s", filename)), UTF_8))
                .lines()
                .collect(joining("\n"));
    }
}
