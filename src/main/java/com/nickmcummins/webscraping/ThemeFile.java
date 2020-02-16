package com.nickmcummins.webscraping;

import java.io.FileWriter;
import java.io.IOException;

public interface ThemeFile {
    String getName();

    String getExtension();

    default void writeToFile() {
        String filename = String.format("%s.%s", getName().replaceAll(" ", "-"), getExtension());
        try (FileWriter file = new FileWriter(filename)) {
            file.write(toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(String.format("Successfully wrote %s", filename));
        }
    }
}
