package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.com.jetbrains.IclsColorScheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.converter.EclipseToIclsThemeConverter;

import java.io.IOException;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(name = "convert")
public class EclipseColorThemeConvertCommand implements Runnable {
    @Option(names = {"-i", "--xml-file"}, required = true)
    public String xmlFile;

    @Override
    public void run() {

        try {
            EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromXmlFile(xmlFile);
            IclsColorScheme icls = new EclipseToIclsThemeConverter().convert(eclipseColorTheme);
            icls.writeToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
