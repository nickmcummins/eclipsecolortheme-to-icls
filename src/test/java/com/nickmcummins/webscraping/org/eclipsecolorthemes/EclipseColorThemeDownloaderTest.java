package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.testng.Assert.assertEquals;

public class EclipseColorThemeDownloaderTest {
    @Test
    public void testDownloadLight() throws CannotDownloadException, IOException {
        EclipseColorTheme eclipseColorTheme = EclipseColorThemeDownloader.downloadTheme(6083, false);
        assertEquals(eclipseColorTheme.toString(), loadResourceAsString("LightGreenCPlusPlus.xml"));
    }

    public static String loadResourceAsString(String resourceName) throws IOException {
        String resourceFilename = new File(Objects.requireNonNull(EclipseColorThemeDownloaderTest.class.getClassLoader().getResource(resourceName)).getFile()).getAbsolutePath();
        return new String(Files.readAllBytes(Paths.get(resourceFilename)));
    }
}