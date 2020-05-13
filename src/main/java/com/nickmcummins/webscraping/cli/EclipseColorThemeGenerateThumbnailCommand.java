package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeThumbnail;

import java.util.List;

import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getDownloadedXmls;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "thumbnail")
public class EclipseColorThemeGenerateThumbnailCommand implements Runnable {
    public static final String THUMBNAILS_DIRECTORY = String.format("%s/thumbnails", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY);

    @Option(names = {"-i", "--xml-file"})
    public String xmlFilename;

    @Override
    public void run() {
        List<String> xmlFilenames = (xmlFilename) != null ? List.of(xmlFilename) : getDownloadedXmls();
        for (String xmlFile : xmlFilenames) {
            EclipseColorThemeThumbnail thumbnail = new EclipseColorThemeThumbnail(xmlFile);
            thumbnail.generate();
        }
    }
}
