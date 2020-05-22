package com.nickmcummins.webscraping.org.eclipsecolorthemes;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.buildobjects.process.ProcBuilder;
import org.buildobjects.process.ProcResult;
import org.jsoup.internal.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.nickmcummins.webscraping.Util.print;
import static com.nickmcummins.webscraping.cli.EclipseColorThemeGenerateThumbnailCommand.THUMBNAILS_DIRECTORY;

public class EclipseColorThemeThumbnail {
    private static final String wkhtmltoimage = "wkhtmltoimage";
    private static final List<String> DEFAULT_OPTS = List.of("--crop-w", "362");

    private final String xmlFilepath;
    private final String imageFilename;
    private final Path cssFilepath;
    private final Path htmlFilepath;

    public EclipseColorThemeThumbnail(String xmlFilepath, String thumbnailsDirectory) {
        this.xmlFilepath = xmlFilepath;
        String[] xmlFilepathParts = xmlFilepath.split("/");
        String imageFilename = xmlFilepathParts[xmlFilepathParts.length - 1].replace(".xml", ".png");

        if (thumbnailsDirectory.equals(THUMBNAILS_DIRECTORY)) {
            String xmlFileDirectory = xmlFilepathParts[xmlFilepathParts.length - 2];
            String imageFilePrefix = NumberUtils.isDigits(xmlFileDirectory) ? String.format("%s-", xmlFileDirectory) : "";
            this.imageFilename = String.format("%s/%s%s", thumbnailsDirectory, imageFilePrefix, imageFilename);
        }
        else
            this.imageFilename = String.format("%s/%s", thumbnailsDirectory, imageFilename);
        try {
            Path tmpDirectory = Files.createTempDirectory("eclipsecolortheme_thumbnail_");
            this.cssFilepath = Files.createTempFile(tmpDirectory, null, ".css");
            this.htmlFilepath = Files.createTempFile(tmpDirectory, null, ".html");
        } catch (IOException e) {
            throw new RuntimeException("Exception writing thumbnail HTML/CSS files", e);
        }
    }

    public void generate() {
        if (new File(imageFilename).exists()) {
            print("\tSkipping creation of already existing file %s", imageFilename);
        } else {
            EclipseColorThemeThumbnailCss previewCss = new EclipseColorThemeThumbnailCss(xmlFilepath);
            try (FileWriter cssFile = new FileWriter(cssFilepath.toFile()); FileWriter htmlFile = new FileWriter(htmlFilepath.toFile())) {
                cssFile.write(previewCss.toString());
                System.out.println(String.format("\tSuccessfully wrote %s", cssFilepath));

                String previewHtml = IOUtils.toString(new FileInputStream("src/main/resources/eclipsecolortheme-preview.html"), StandardCharsets.UTF_8).replace("eclipsecolortheme.css", cssFilepath.toString());
                htmlFile.write(previewHtml);
                System.out.println(String.format("\tSuccessfully wrote %s", htmlFilepath));
            } catch (IOException e) {
                throw new RuntimeException("Exception generating thumbnail file", e);
            }

            wkhtmltoimageCmd();
            pngoutCmd();
        }
    }

    private void wkhtmltoimageCmd() {

        List<String> args = new ArrayList<>(DEFAULT_OPTS.size() + 2);
        args.addAll(DEFAULT_OPTS);
        args.add(htmlFilepath.toString());
        args.add(imageFilename);

        System.out.println(String.format("\tExecuting %s %s", wkhtmltoimage, StringUtil.join(args, " ")));

        ProcResult result = new ProcBuilder("wkhtmltoimage")
                .withArgs(args.toArray(new String[0]))
                .run();
        if (new File(imageFilename).exists())
            print("\tSuccessfully wrote %s", imageFilename);
        else
            throw new RuntimeException(String.format("Error generating %s: %s", imageFilename, result.getErrorString()));
    }

    private void pngoutCmd() {
        ProcResult result = new ProcBuilder("pngout")
                .withArg(imageFilename)
                .withNoTimeout()
                .run();
        print("\tExecuting pngout %s", imageFilename);
    }
}
