package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.*;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class EclipseToIntellijIdeaThemeConverterTest {
    ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> eclipseToIntellijThemeConverter;

    @BeforeClass
    public void setup() {
        eclipseToIntellijThemeConverter = new EclipseToIntellijIdeaThemeConverter();
    }

    @DataProvider
    public Object[][] lightThemes() {
        return new Object[][] {
                new Object[] { "github-theme.xml" }
        };
    }

    @Test(dataProvider = "lightThemes")
    public void testConvertLightTheme(String lightThemeFilename) throws Exception {
        String eclipseColorThemeXml = loadResourceAsString(lightThemeFilename);
        String expectedIcls = loadResourceAsString(lightThemeFilename.replace("xml", "icls"));
        IntellijIdeaColorScheme convertedIcls = eclipseToIntellijThemeConverter.convert(EclipseColorTheme.fromString(eclipseColorThemeXml));
        convertedIcls.updateMetaInfo(created, "2020-05-13T14:05:37");
        convertedIcls.updateMetaInfo(ide, "idea");
        convertedIcls.updateMetaInfo(ideVersion, "2020.1.1.0.0");
        convertedIcls.updateMetaInfo(modified, "2020-05-13T14:06:05");
        assertEquals(convertedIcls.toString(), expectedIcls);
    }
}
