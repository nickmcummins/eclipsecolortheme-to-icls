package com.nickmcummins.webscraping.persistence.yaml;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.CannotDownloadException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.Util.get;

public class YamlFilePersistence {
    public static final String OUTPUT_DIR = String.format("%s/Downloads/Themes/eclipse-color-themes/downloader", System.getProperty("user.home"));
    private static final String THEME_INDEX = String.format("%s/themes.yaml", OUTPUT_DIR);
    private static final String PAGES_INDEX = String.format("%s/page-list.yaml", OUTPUT_DIR);
    private static final String DOWNLOADED_PAGES_INDEX = String.format("%s/downloaded-pages.yaml", OUTPUT_DIR);

    private static final Yaml YAML;
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(options);
    }
    private final Map<Integer, String> downloadedThemesIndex;
    private List<String> pageListIndex;
    private List<String> downloadedPagesIndex;


    public YamlFilePersistence() {
        this.downloadedThemesIndex = loadDownloadedThemesIndex();
        this.pageListIndex = loadPagesIndex(PAGES_INDEX);
        this.downloadedPagesIndex = loadPagesIndex(DOWNLOADED_PAGES_INDEX);

    }

    private Map<Integer, String> loadDownloadedThemesIndex() {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(THEME_INDEX))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded theme index", e);
        }
    }

    private List<String> loadPagesIndex(String pageIndex) {
        try {
            return YAML.load(new String(Files.readAllBytes(Paths.get(pageIndex))));
        } catch (IOException e) {
            throw new RuntimeException("Exception loading downloaded pages index", e);
        }
    }

    public List<String> updatePageIndex(String startPage) throws IOException, InterruptedException, CannotDownloadException {
        Document ectPage = Jsoup.parse(get(startPage));
        List<String> pageUrls = new ArrayList<>(ectPage.select("div[class='selector'] li a").stream()
                .map(a -> a.attr("href"))
                .filter(url -> url.contains("page="))
                .collect(Collectors.toSet()));
        this.downloadedPagesIndex = pageUrls;
        writePageListIndex();
        return pageUrls;

    }

    public void writeThemeIndex()
    {
        writeIndex(THEME_INDEX);
    }

    public void writePageListIndex()
    {
        writeIndex(PAGES_INDEX);
    }

    private void writeIndex(String index)
    {
        try (FileWriter file = new FileWriter(index)) {
            StringWriter writer = new StringWriter();
            YAML.dump(index.equals(THEME_INDEX) ? downloadedThemesIndex : downloadedPagesIndex, writer);
            file.write(writer.toString());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to update downloaded index file %s", index), e);
        }

    }

    public Map<Integer, String> getDownloadedThemesIndex()
    {
        return downloadedThemesIndex;
    }

    public List<String> getPageListIndex() {
        return pageListIndex;
    }

    public List<String> getDownloadedPagesIndex()
    {
        return downloadedPagesIndex;
    }
}
