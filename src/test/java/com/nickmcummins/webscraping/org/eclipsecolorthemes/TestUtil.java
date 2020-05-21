package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class TestUtil {
    public static String loadResourceAsString(String resourceName) throws IOException {
        String resourceFilename = new File(Objects.requireNonNull(TestUtil.class.getClassLoader().getResource(resourceName)).getFile()).getAbsolutePath();
        return new String(Files.readAllBytes(Paths.get(resourceFilename)));
    }

    public static Object[][] toObjectArrayArray(List<String> items) {
        return items.stream()
                .map(item -> new Object[]{item})
                .toArray(Object[][]::new);
    }
}
