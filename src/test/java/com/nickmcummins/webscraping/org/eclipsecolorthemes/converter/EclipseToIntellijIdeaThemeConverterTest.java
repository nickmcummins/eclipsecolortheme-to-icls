package com.nickmcummins.webscraping.org.eclipsecolorthemes.converter;

import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.nickmcummins.webscraping.Util.formatDate;
import static com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme.MetaInfoProperty.*;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.toObjectArrayArray;
import static org.testng.Assert.assertEquals;

public class EclipseToIntellijIdeaThemeConverterTest {
    private ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> eclipseToIntellijThemeConverter;

    @BeforeClass
    public void setup() {
        eclipseToIntellijThemeConverter = new EclipseToIntellijIdeaThemeConverter();
    }

    @DataProvider
    public Object[][] lightThemes() {
        return toObjectArrayArray(List.of(
                "416-berry.xml", "cloud-light-theme.xml", "DefaultPretty.xml", "houlind.xml", "IntelliJ-Purple.xml",
                "LightGreenCPlusPlus.xml", "mads.xml", "Sariizback.xml", "Sholight.xml", "temagossipnerea.xml"
        ));
    }

    @Test(dataProvider = "lightThemes")
    public void testConvertLightTheme(String lightThemeFilename) throws Exception {
        EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromString(loadResourceAsString(lightThemeFilename));
        LocalDateTime modified = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        String expectedIcls = loadResourceAsString(lightThemeFilename.replace("xml", "icls"))
                .replaceAll("2020-05-13T14:06:05", modified.format(IntellijIdeaColorScheme.DATE_FORMAT));

        IntellijIdeaColorScheme convertedIcls = eclipseToIntellijThemeConverter.convert(eclipseColorTheme);
        if (eclipseColorTheme.getModified() == null) {
            convertedIcls.updateMetaInfo(created, modified.format(IntellijIdeaColorScheme.DATE_FORMAT));
        }
        formatDate(IntellijIdeaColorScheme.DATE_FORMAT, modified);
        convertedIcls.updateMetaInfo(IntellijIdeaColorScheme.MetaInfoProperty.modified, modified.format(IntellijIdeaColorScheme.DATE_FORMAT));
        convertedIcls.updateMetaInfo(ide, "idea");
        convertedIcls.updateMetaInfo(ideVersion, "2020.1.1.0.0");
        assertEquals(convertedIcls.toString(), expectedIcls);
    }
}
