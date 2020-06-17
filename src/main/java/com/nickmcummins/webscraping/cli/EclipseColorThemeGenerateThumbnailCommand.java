package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorThemeThumbnail;

import java.util.List;

import static com.nickmcummins.webscraping.Util.listFilesInDirectory;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY;
import static com.nickmcummins.webscraping.persistence.FileIndexUtil.getDownloadedXmls;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "thumbnail")
public class EclipseColorThemeGenerateThumbnailCommand implements Runnable {
    private static final String THUMBNAILS_DIRECTORY = String.format("%s/thumbnails", ECLIPSE_COLOR_THEME_DOWNLOAD_DIRECTORY);

    @Option(names = {"-i", "--xml-file"})
    private String xmlFilename;

    @Option(names = {"-id", "--input-directory"})
    private String inputDirectory;

    @Option(names = {"-od", "--output-directory"})
    private String outputDirectory;

    @Override
    public void run() {
        List<String> xmlFilenames;
        if (xmlFilename != null)
            xmlFilenames = List.of(xmlFilename);
        else if (inputDirectory != null)
            xmlFilenames = listFilesInDirectory(inputDirectory, EclipseColorTheme.EXTENSION);
        else
            xmlFilenames = getDownloadedXmls();

        String thumbnailsDirectory;
        if (outputDirectory != null)
            thumbnailsDirectory = outputDirectory;
        else if (inputDirectory != null)
            thumbnailsDirectory = inputDirectory;
        else
            thumbnailsDirectory = THUMBNAILS_DIRECTORY;

        for (String xmlFile : xmlFilenames) {
            EclipseColorThemeThumbnail thumbnail = new EclipseColorThemeThumbnail(xmlFile, true);
            thumbnail.generate(thumbnailsDirectory);
        }
    }
}
