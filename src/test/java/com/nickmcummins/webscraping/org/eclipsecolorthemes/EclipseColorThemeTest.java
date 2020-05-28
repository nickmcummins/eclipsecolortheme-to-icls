package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.testng.annotations.Test;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class EclipseColorThemeTest {
    @Test(enabled = false)
    public void testToColorsYaml() throws Exception {
        assertEquals(EclipseColorTheme.fromString(loadResourceAsString("github-theme.xml")).toColorsYaml(), "");
    }
}
