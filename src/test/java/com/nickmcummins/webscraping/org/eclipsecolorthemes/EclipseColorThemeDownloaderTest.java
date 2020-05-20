package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class EclipseColorThemeDownloaderTest {
    @Test(enabled = false)
    public void testDownloadLight() throws CannotDownloadException, IOException {
        EclipseColorTheme eclipseColorTheme = EclipseColorThemeDownloader.downloadTheme(6083, false);
        assertEquals(eclipseColorTheme.toString(), loadResourceAsString("LightGreenCPlusPlus.xml"));
    }
}