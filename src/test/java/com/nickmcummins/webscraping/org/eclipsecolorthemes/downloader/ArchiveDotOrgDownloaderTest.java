package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import org.testng.annotations.Test;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class ArchiveDotOrgDownloaderTest {
    @Test(enabled = false)
    public void testDownloadLight() throws Exception {
        EclipseColorTheme eclipseColorTheme = new ArchiveDotOrgDownloader().downloadTheme(6083);
        assertEquals(eclipseColorTheme.toString(), loadResourceAsString("LightGreenCPlusPlus.xml"));
    }
}