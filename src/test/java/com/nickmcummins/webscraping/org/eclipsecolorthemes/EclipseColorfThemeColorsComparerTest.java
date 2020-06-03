package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.testng.annotations.Test;

import java.util.List;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertTrue;

public class EclipseColorfThemeColorsComparerTest {
    @Test(enabled = false)
    public void test() throws Exception {
        EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromString(loadResourceAsString("LightGreenCPlusPlus.xml"));
        List<List<Integer>> colorSumsAndDiffs = new EclipseColorThemeColorsComparer(eclipseColorTheme).computeColorSumsAndDiffs();
        assertTrue(colorSumsAndDiffs.contains(List.of(125,184,124)));


    }
}
