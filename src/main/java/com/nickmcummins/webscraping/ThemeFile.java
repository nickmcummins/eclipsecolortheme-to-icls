package com.nickmcummins.webscraping;

import java.io.FileWriter;
import java.io.IOException;

public interface ThemeFile {
    String getName();

    String getExtension();

    default void writeToFile() throws IOException {
        writeToFile("");
    }

    default String writeToFile(String directory) {
        directory = String.format("%s/", directory);
        String filename = String.format("%s%s.%s", directory, getName().replaceAll(" ", "-"), getExtension());
        try (FileWriter file = new FileWriter(filename)) {
            file.write(toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(String.format("Successfully wrote %s", filename));
        return filename;
    }
}
