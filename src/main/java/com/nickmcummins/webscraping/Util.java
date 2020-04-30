package com.nickmcummins.webscraping;

import com.nickmcummins.webscraping.org.eclipsecolorthemes.CannotDownloadException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.Thread.sleep;

public class Util {
    private static final int MAX_RETRIES = 5;

    public static String get(String url) throws InterruptedException, CannotDownloadException {
        HttpClient requests = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        boolean success = false;
        int tries = 0;
        do {
            if (response != null && response.statusCode() == 302)
                url = response.headers().allValues("location").get(0);
            try {
                tries += 1;
                response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200)
                    success = true;
                else if (response.statusCode() == 404) {
                    System.out.println(String.format("Received 404 attempting to download %s; skipping", url));
                    throw new CannotDownloadException();
                }
            } catch (IOException ce) {
                if (ce instanceof ConnectException || ce.getMessage().equals("Received RST_STREAM: Internal error")) {
                    System.out.println(String.format("Connection exception when attempting to download %s. Sleeping 10 seconds.", url));
                    sleep(10000);
                }
            }
        } while ((response != null && response.statusCode() == 302) || (!success && tries < MAX_RETRIES));
        if (response == null)
            throw new CannotDownloadException();

        return response.body();
    }

    public static void print(String message, Object... args)
    {
        System.out.println(String.format(message, args));
    }
}
