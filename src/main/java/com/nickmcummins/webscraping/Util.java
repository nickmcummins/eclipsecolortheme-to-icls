package com.nickmcummins.webscraping;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public class Util {
    public static final Yaml YAML;
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(options);
    }

    public static void print(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    public static void print(Object object) {
        System.out.println(object);
    }

    public static String formatDate(DateTimeFormatter dateFormat, LocalDateTime date) {
        String formattedDate = dateFormat.format(LocalDateTime.now());
        if (date != null) {
            try {
                formattedDate = dateFormat.format(date);
            } catch (Exception e) {
                print("Exception attempting to format %s as a date string: %s\nUsing current datetime of %s as date value.", date, e.getMessage(), formattedDate);
            }
        }
        return formattedDate;
    }

    public static LocalDateTime parseDate(DateTimeFormatter dateFormat, String dateString) {
        try {
            return LocalDateTime.parse(dateString, dateFormat);
        } catch (DateTimeParseException dtpe) {
            return null;
        }
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

    public static String replaceValueOfXmlElement(String xml, String elementName, String attrName, String attrValue, String newValue)  {
        String xmlOpenTag = String.format("<%s %s=\"%s\">", elementName, attrName, attrValue);
        String xmlCloseTag = String.format("</%s>", elementName);

        int valIndexOfStart = xml.indexOf(xmlOpenTag) + xmlOpenTag.length();
        int valIndexOfEnd = xml.indexOf(xmlCloseTag, valIndexOfStart);

        String currentVal = xml.substring(valIndexOfStart, valIndexOfEnd);
        return xml.replaceFirst(currentVal, newValue);
    }
}
