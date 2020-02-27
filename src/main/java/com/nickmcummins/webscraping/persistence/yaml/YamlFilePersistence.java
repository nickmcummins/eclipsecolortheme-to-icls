package com.nickmcummins.webscraping.persistence.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class YamlFilePersistence {
    public static final String OUTPUT_DIR = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));
    private static final String THEME_INDEX = String.format("%s/themes.yaml", OUTPUT_DIR);
    private static final String PAGES_INDEX = String.format("%s/pages.yaml", OUTPUT_DIR);
    private static final Yaml YAML;
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(options);
    }
    private final Map<Integer, String> downloadedThemesIndex;
    private final Map<String, Map<Integer, List<String>>> downloadedPagesIndex;

    public YamlFilePersistence() {
        this.downloadedThemesIndex = loadDownloadedThemesIndex();
        this.downloadedPagesIndex = loadDownloadedPagesIndex();
    }

    private Map<Integer, String> loadDownloadedThemesIndex() {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(THEME_INDEX))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded theme index", e);
        }
    }

    private Map<String, Map<Integer, List<String>>> loadDownloadedPagesIndex() {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(PAGES_INDEX))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded pages index", e);
        }
    }

    public void writeDownloadedThemesIndex()
    {
        try (FileWriter file = new FileWriter(THEME_INDEX)) {
            StringWriter writer = new StringWriter();
            YAML.dump(downloadedThemesIndex, writer);
            file.write(writer.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to update downloaded theme index file", e);
        }
    }

    public Map<Integer, String> getDownloadedThemesIndex()
    {
        return downloadedThemesIndex;
    }

    public Map<String, Map<Integer, List<String>>> getDownloadedPagesIndex()
    {
        return downloadedPagesIndex;
    }
}
