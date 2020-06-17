package com.nickmcummins.webscraping.http;

public class CannotDownloadException extends Exception {
    public  CannotDownloadException() {
        super();
    }

    public CannotDownloadException(Throwable cause) {
        super(cause);
    }
}
