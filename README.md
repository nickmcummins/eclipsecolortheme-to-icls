# eclipsecolortheme-to-icls
Convert eclipsecolorthemes.org XML to IntelliJ IDEA Color Scheme (.icls)

## Usage
```
Usage: eclipsecolortheme-cli [COMMAND]
Commands:
  download   Download from eclipsecolorthemes.org.
  convert
  thumbnail
```

### Downloader Tool
This is for downloading themes from eclipsecolorthemes.org. 
```
Usage: eclipsecolortheme-cli download [-lt] [-n=<numberToDownload>]
                                      [-s=<startId>]
Download from eclipsecolorthemes.org.
  -l, --use-last-id
  -n, --number-to-download=<numberToDownload>
  -s, --start-id=<startId>
  -t, --generate-thumbnails

```
### Thumbnailer 
```
Usage: eclipsecolortheme-cli thumbnail [-i=<xmlFilename>]
                                       [-id=<inputDirectory>]
                                       [-od=<outputDirectory>]
  -i, --xml-file=<xmlFilename>
  -id, --input-directory=<inputDirectory>
  -od, --output-directory=<outputDirectory>

```

### Converter (WIP)
```
Usage: eclipsecolortheme-cli convert -i=<xmlFile>
  -i, --xml-file=<xmlFile>

```
