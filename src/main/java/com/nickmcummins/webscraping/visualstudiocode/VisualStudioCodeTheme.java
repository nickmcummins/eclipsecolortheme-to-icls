package com.nickmcummins.webscraping.visualstudiocode;

import com.nickmcummins.webscraping.ColorTheme;

import java.util.List;

public class VisualStudioCodeTheme implements ColorTheme {
    private static final List<String> DEFAULT_TAGS = List.of("theme", "color-theme");
    private static final String GITHUB_REPO = "https://github.com/nickmcummins/eclipsecolortheme-to-icls";

    private final String id;
    private final String name;
    private final String publisher;
    private final List<String> tags;

    public VisualStudioCodeTheme(String id, String name, String publisher) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.tags = DEFAULT_TAGS;
    }

    private String extensionVsixManifest() {
        return String.format("""
                <?xml version="1.0" encoding="utf-8"?>
                <PackageManifest Version="2.0.0" xmlns="http://schemas.microsoft.com/developer/vsx-schema/2011" xmlns:d="http://schemas.microsoft.com/developer/vsx-schema-design/2011">
                    <Metadata>
                        <Identity Language="en-US" Id="theme-vitesse" Version="0.1.0" Publisher="%s"/>
                        <DisplayName>%s</DisplayName>
                        <Description xml:space="preserve">%s for VS Code</Description>
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
                """, publisher, name, name, String.join(",", tags), GITHUB_REPO, GITHUB_REPO, GITHUB_REPO, GITHUB_REPO);
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
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }
}
