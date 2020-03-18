package com.nickmcummins.webscraping.cli;

import picocli.CommandLine;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Spec;
import static picocli.CommandLine.Model.CommandSpec;

@Command(name = "eclipsecolortheme", subcommands = {EclipseColorThemeDownload.class })
public class EclipseColorTheme implements Runnable {
    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new EclipseColorTheme());
        cmd.execute(args);
    }
}
