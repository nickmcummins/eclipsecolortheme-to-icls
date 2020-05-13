package com.nickmcummins.webscraping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface ColorTheme {
    List<String> INVALID_CHARACTERS = List.of(" ", "/");

    String getName();

    String getExtension();

    default void writeToFile() throws IOException {
        writeToFile("");
    }

    default String writeToFile(String directory) {
        directory = String.format("%s/", directory);
        String filename = String.format("%s%s.%s", directory, sanitizeFilename(getName()), getExtension());
        try (FileWriter file = new FileWriter(filename)) {
            file.write(toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(String.format("Successfully wrote %s", filename));
        return filename;
    }

    static String sanitizeFilename(String filename) {
        for (String invalidCharacter : INVALID_CHARACTERS)
            filename = filename.replaceAll(invalidCharacter, "-");
        return filename;
    }
}
