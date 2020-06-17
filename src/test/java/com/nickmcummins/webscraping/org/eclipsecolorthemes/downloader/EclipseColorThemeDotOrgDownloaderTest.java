package com.nickmcummins.webscraping.org.eclipsecolorthemes.downloader;

import org.testng.annotations.Test;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class EclipseColorThemeDotOrgDownloaderTest {
    @Test(enabled = false)
    public void testDownloadThemeXml() throws Exception {
        EclipseColorThemeDotOrgDownloader eclipseColorThemeDownloader = new EclipseColorThemeDotOrgDownloader();
        String downloadedXml = eclipseColorThemeDownloader.downloadThemeXml(6083);
        assertEquals(downloadedXml, loadResourceAsString("LightGreenCPlusPlus.xml"));
    }
}
