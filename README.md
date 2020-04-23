# eclipsecolortheme-to-icls
Convert eclipsecolorthemes.org XML to IntelliJ IDEA Color Scheme (.icls)

## Usage
```
Usage: eclipsecolortheme [COMMAND]
Commands:
  download  Download from eclipsecolorthemes.org.
  convert
```

### Downloader Tool
This is for downloading themes from eclipsecolorthemes.org. 
```
Usage: eclipsecolortheme download [-l] [-n=<numberToDownload>] [-s=<startId>]
   Download from eclipsecolorthemes.org.
     -l, --use-last-id
     -n, --number-to-download=<numberToDownload>
   
     -s, --start-id=<startId>
```
### Thumbnailer 
```
Usage: eclipsecolortheme thumbnail [-i=<xmlFilename>]
  -i, --xml-file=<xmlFilename>
```

### Converter (WIP)
```
Usage: eclipsecolortheme convert -i=<xmlFile> -t=<type>
  -i, --xml-file=<xmlFile>
  -t, --light-or-dark=<type>

```
