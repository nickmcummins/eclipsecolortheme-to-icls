package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.ColorTheme;
import com.nickmcummins.webscraping.com.jetbrains.IclsColorScheme;
import com.nickmcummins.webscraping.converter.ThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.converter.EclipseToIclsThemeConverter;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.converter.EclipseToVSCodeThemeConverter;

import java.io.IOException;

import static com.nickmcummins.webscraping.cli.EclipseColorThemeConvertCommand.OutputFormat.*;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(name = "convert")
public class EclipseColorThemeConvertCommand implements Runnable {
    @Option(names = {"-i", "--xml-file"}, required = true)
    public String xmlFile;

    @Option(names = {"-of", "--output-format"}, required = true)
    public OutputFormat outputFormat;

    @Override
    public void run() {

        try {
            EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromXmlFile(xmlFile);
            ThemeConverter themeConverter = null;
            if (outputFormat == ICLS) themeConverter = new EclipseToIclsThemeConverter();
            else if (outputFormat == VSCODE) themeConverter = new EclipseToVSCodeThemeConverter();
            ColorTheme convertedTheme = themeConverter.convert(eclipseColorTheme);

            convertedTheme.writeToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum OutputFormat {
        ICLS,
        VSCODE
    }
}
