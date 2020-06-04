package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.http.CannotDownloadException;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class ArchiveOrgEclipseColorThemeDownloaderTest {
    @Test(enabled = false)
    public void testDownloadLight() throws CannotDownloadException, IOException {
        EclipseColorTheme eclipseColorTheme = new ArchiveOrgEclipseColorThemeDownloader().downloadTheme(6083);
        assertEquals(eclipseColorTheme.toString(), loadResourceAsString("LightGreenCPlusPlus.xml"));
    }
}