package com.nickmcummins.webscraping.visualstudiocode;

import com.nickmcummins.webscraping.ColorTheme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.joining;

public class VisualStudioCodeTheme implements ColorTheme {
    private static final List<String> DEFAULT_TAGS = List.of("theme", "color-theme");
    private static final String GITHUB_REPO = "https://github.com/nickmcummins/eclipsecolortheme-to-icls";
    public static final String EXTENSION = "vsix";

    private final String id;
    private final String name;
    private final String publisher;
    private final List<String> tags;
    private List<VSCodeColor> colors;
    private List<VSCodeTokenColor> tokenColors;

    public VisualStudioCodeTheme(String id, String name, String publisher, List<VSCodeColor> colors, List<VSCodeTokenColor> tokenColors) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.tags = DEFAULT_TAGS;
        this.colors = colors;
        this.tokenColors = tokenColors;
    }

    private String packageJson() {
        return String.format("""
                {
                  "name": "%s",
                  "displayName": "%s",
                  "version": "0.1.0",
                  "description": "%s theme for VS Code",
                  "categories": ["Themes"],
                  "keywords": ["theme"],
                  "bugs": {"url": "%s/issues"},
                  "repository": {
                    "type": "git",
                    "url": "%s"
                  },
                  "license": "MIT",
                  "publisher": "%s",
                  "scripts": {
                    "build": "esno src/index.ts",
                    "dev": "nodemon --watch src -e ts --exec \\"esno src/index.ts\\"",
                    "vscode:prepublish": "npm run build",
                    "release": "npx bumpp --commit --tag --push && vsce publish"
                  },
                  "contributes": {
                    "themes": [
                      {
                        "label": "%s",
                        "uiTheme": "vs",
                        "path": "./themes/%s.json"
                      }
                    ]
                  },
                  "devDependencies": {
                    "@antfu/eslint-config": "^0.4.3",
                    "@types/color": "^3.0.1",
                    "color": "^3.1.2",
                    "eslint": "^7.21.0",
                    "esno": "^0.4.6",
                    "nodemon": "^2.0.3",
                    "tsup": "^4.6.1",
                    "typescript": "^4.2.3"
                  },
                  "engines": {
                    "vscode": "^1.43.0"
                  },
                  "icon": "icon.png",
                  "preview": true
                }
                """, id, name, name, GITHUB_REPO, GITHUB_REPO, publisher, name, id);
    }

    private String extensionVsixManifest() {
        return String.format("""
                <?xml version="1.0" encoding="utf-8"?>
                <PackageManifest Version="2.0.0" xmlns="http://schemas.microsoft.com/developer/vsx-schema/2011" xmlns:d="http://schemas.microsoft.com/developer/vsx-schema-design/2011">
                    <Metadata>
                        <Identity Language="en-US" Id="%s" Version="0.1.0" Publisher="%s"/>
                        <DisplayName>%s</DisplayName>
                        <Description xml:space="preserve">%s theme for VS Code</Description>
                        <Tags>%s</Tags>
                        <Categories>Themes</Categories>
                        <GalleryFlags>Public Preview</GalleryFlags>
                        <Badges></Badges>
                        <Properties>
                            <Property Id="Microsoft.VisualStudio.Code.Engine" Value="^1.43.0"/>
                            <Property Id="Microsoft.VisualStudio.Code.ExtensionDependencies" Value=""/>
                            <Property Id="Microsoft.VisualStudio.Code.ExtensionPack" Value=""/>
                            <Property Id="Microsoft.VisualStudio.Code.ExtensionKind" Value="ui,workspace,web"/>
                            <Property Id="Microsoft.VisualStudio.Code.LocalizedLanguages" Value=""/>
                                
                            <Property Id="Microsoft.VisualStudio.Services.Links.Source" Value="%s"/>
                            <Property Id="Microsoft.VisualStudio.Services.Links.Getstarted" Value="%s"/>
                                
                            <Property Id="Microsoft.VisualStudio.Services.Links.GitHub" Value="%s"/>
                            <Property Id="Microsoft.VisualStudio.Services.Links.Support" Value="%s/issues"/>
                            <Property Id="Microsoft.VisualStudio.Services.GitHubFlavoredMarkdown" Value="true"/>
                        </Properties>
                        <License>extension/LICENSE.txt</License>
                        <Icon>extension/icon.png</Icon>
                    </Metadata>
                    <Installation>
                        <InstallationTarget Id="Microsoft.VisualStudio.Code"/>
                    </Installation>
                    <Dependencies/>
                    <Assets>
                        <Asset Type="Microsoft.VisualStudio.Code.Manifest" Path="extension/package.json" Addressable="true"/>
                        <Asset Type="Microsoft.VisualStudio.Services.Content.Details" Path="extension/README.md" Addressable="true"/>
                        <Asset Type="Microsoft.VisualStudio.Services.Content.License" Path="extension/LICENSE.txt" Addressable="true"/>
                        <Asset Type="Microsoft.VisualStudio.Services.Icons.Default" Path="extension/icon.png" Addressable="true"/>
                    </Assets>
                </PackageManifest>          
                """, id, publisher, name, name, String.join(",", tags), GITHUB_REPO, GITHUB_REPO, GITHUB_REPO, GITHUB_REPO);
    }

    private static String contentTypesXml() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                    <Default Extension=".png" ContentType="image/png"/>
                    <Default Extension=".txt" ContentType="text/plain"/>
                    <Default Extension=".json" ContentType="application/json"/>
                    <Default Extension=".md" ContentType="text/markdown"/>
                    <Default Extension=".ts" ContentType="video/mp2t"/>
                    <Default Extension=".vsixmanifest" ContentType="text/xml"/>
                </Types>
                """;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    public String toString() {
        return String.format("""
                {
                    "name": "%s",
                    "semanticHighlighting": true,
                    "$schema": "vscode://schemas/color-theme",
                    "type": "light",
                    "colors": {
                        %s
                    },
                    "tokenColors": {
                        %s
                    }
                }
                """,
                name,
                colors.stream().map(VSCodeColor::toString).collect(joining(",\n")),
                tokenColors.stream().map(VSCodeTokenColor::toString).collect(joining(",\n")));
    }

    public void writeToFile() throws IOException {
        String outputDir = "converted";
        Files.createDirectory(Paths.get(outputDir));
        ColorTheme.super.writeToFile(outputDir);
    }
}
