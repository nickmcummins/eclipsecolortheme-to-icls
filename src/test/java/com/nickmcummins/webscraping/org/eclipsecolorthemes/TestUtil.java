package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestUtil {
    public static String loadResourceAsString(String resourceName) throws IOException {
        String resourceFilename = new File(Objects.requireNonNull(TestUtil.class.getClassLoader().getResource(resourceName)).getFile()).getAbsolutePath();
        return new String(Files.readAllBytes(Paths.get(resourceFilename)));
    }
}
