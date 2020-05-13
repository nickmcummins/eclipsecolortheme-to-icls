package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.converter.EclipseToIntellijIdeaThemeConverter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.testng.Assert.assertEquals;

public class EclipseToIntellijIdeaThemeConverterTest {
    ThemeConverter<EclipseColorTheme, IntellijIdeaColorScheme> eclipseToIntellijThemeConverter;

    @BeforeClass
    public void setup() {
        eclipseToIntellijThemeConverter = new EclipseToIntellijIdeaThemeConverter();
    }

    @Test(enabled = false)
    public void testConvertLightTheme() throws Exception {
        String eclipseColorThemeXml = loadResourceAsString("github-theme.xml");
        String expectedIcls = loadResourceAsString("github-theme.icls");
        IntellijIdeaColorScheme convertedIcls = eclipseToIntellijThemeConverter.convert(EclipseColorTheme.fromString(eclipseColorThemeXml));
        convertedIcls.updateMetaInfo("created", "2020-05-13T14:05:37");
        convertedIcls.updateMetaInfo("ide", "idea");
        convertedIcls.updateMetaInfo("ideVersion", "2020.1.1.0.0");
        convertedIcls.updateMetaInfo("modified", "2020-05-13T14:06:05");
        assertEquals(convertedIcls, expectedIcls);
    }

    public static String loadResourceAsString(String resourceName) throws IOException {
        String resourceFilename = new File(Objects.requireNonNull(EclipseToIntellijIdeaThemeConverterTest.class.getClassLoader().getResource(resourceName)).getFile()).getAbsolutePath();
        return new String(Files.readAllBytes(Paths.get(resourceFilename)));
    }
}
