package com.nickmcummins.webscraping;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public interface ColorTheme {
    List<String> INVALID_CHARACTERS = List.of(" ", "/");

    String getName();

    default String getFilename() {
        return String.format("%s%s.%s", getFilenamePrefix(), sanitizeFilename(getName()), getExtension());
    }

    default String getFilenamePrefix() {
        return "";
    }

    String getExtension();

    default String writeToFile(Path directoryPath) {
        return Util.writeToFile(directoryPath, getFilename(), toString());
    }

    default void writeToFile(String downloadDirectory) {
        writeToFile(Paths.get(downloadDirectory));
    }

    static String sanitizeFilename(String filename) {
        for (String invalidCharacter : INVALID_CHARACTERS)
            filename = filename.replaceAll(invalidCharacter, "-");
        return filename;
    }
}
