package com.nickmcummins.webscraping.cli;

import com.nickmcummins.webscraping.com.jetbrains.AttributeOption;
import com.nickmcummins.webscraping.com.jetbrains.IntellijIdeaColorScheme;
import com.nickmcummins.webscraping.com.jetbrains.SchemeType;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.ColorThemeElement;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.ThemeConverter.*;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(name = "convert")
public class EclipseColorThemeConvertCommand implements Runnable {
    @Option(names = {"-i", "--xml-file"}, required = true)
    public String xmlFile;

    @Option(names = {"-t", "--light-or-dark"}, required = true)
    public SchemeType type;

    @Override
    public void run() {
        Map<String, String> iclsColorOptions = new HashMap<>();
        List<AttributeOption> iclsAttributeOptions = new ArrayList<>();

        EclipseColorTheme eclipseColorTheme = EclipseColorTheme.fromXmlFile(xmlFile);
        for (Map.Entry<String, ColorThemeElement> colorThemeElement : eclipseColorTheme.getSettingsByName().entrySet()) {
            String eclipseFieldName = colorThemeElement.getKey();
            ColorThemeElement eclipseColor = colorThemeElement.getValue();
            if (ECLIPSE_TO_IDEA_OPTIONS.containsKey(eclipseFieldName)) {
                List<String> iclsOptionsWithColor = ECLIPSE_TO_IDEA_OPTIONS.get(eclipseFieldName);
                for (String iclsColorOption : iclsOptionsWithColor)
                    iclsColorOptions.put(iclsColorOption, formatHexValue(eclipseColor.getColorValue()));
            } else if (ECLIPSE_TO_IDEA_ATTRIBUTES.containsKey(eclipseFieldName)) {
                for (String iclsAttributeOptionName : ECLIPSE_TO_IDEA_ATTRIBUTES.get(eclipseFieldName)) {
                    Map<String, String> attributeOptions = new HashMap<>();
                    attributeOptions.put("FOREGROUND", eclipseColor.getColorValue());
                    if (eclipseColor.isBold())
                        attributeOptions.put("FONT_TYPE", "1");
                    if (eclipseColor.isItalic())
                        attributeOptions.put("FONT_TYPE", "2");
                    if (eclipseColor.isStrikethrough())
                        attributeOptions.put("EFFECT_TYPE", "3");
                    iclsAttributeOptions.add(new AttributeOption(iclsAttributeOptionName, attributeOptions));
                }
            } else
                System.out.println(String.format("Skipping unmapped %s in Eclipse XML.", eclipseFieldName));
        }
        iclsAttributeOptions.addAll(ICLS_CONSOLE_DEFAULTS.get(type));

        IntellijIdeaColorScheme icls = new IntellijIdeaColorScheme(
                eclipseColorTheme.getModified(),
                eclipseColorTheme.getName(),
                iclsColorOptions,
                iclsAttributeOptions);
        try {
            icls.writeToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(icls);
    }
}
