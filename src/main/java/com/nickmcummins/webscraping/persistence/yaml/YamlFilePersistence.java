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
    private final List<String> downloadedPagesIndex;
    private final Map<String, Object> INDEXES = Map.of(
            THEME_INDEX, (Object)downloadedThemesIndex,
            PAGES_INDEX, (Object)downloadedPagesIndex
    )

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

    private List<String> loadDownloadedPagesIndex() {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(PAGES_INDEX))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded pages index", e);
        }
    }

    public void writeIndexes()
    {
        for (String index : INDEXES) {
            try (FileWriter file = new FileWriter(index)) {
                StringWriter writer = new StringWriter();
                YAML.dump(downloadedThemesIndex, writer);
                file.write(writer.toString());
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to update downloaded index file %s", index), e);
            }
        }
    }

    public Map<Integer, String> getDownloadedThemesIndex()
    {
        return downloadedThemesIndex;
    }

    public List<String> getDownloadedPagesIndex()
    {
        return downloadedPagesIndex;
    }
}
