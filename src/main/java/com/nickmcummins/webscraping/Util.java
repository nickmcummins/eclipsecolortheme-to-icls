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
    public static String get(String url) throws IOException, InterruptedException, CannotDownloadException {
        HttpClient requests = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        boolean success = false;
        do {
            if (response != null && response.statusCode() == 302)
                url = response.headers().allValues("location").get(0);
            try {
                response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200)
                    success = true;
                else if (response.statusCode() == 404) {
                    System.out.println(String.format("Received 404 attempting to download %s; skipping", url));
                    throw new CannotDownloadException();
                }
            } catch (ConnectException ce) {
                System.out.println(String.format("Connection exception when attempting to download %s. Sleeping 10 seconds.", url));
                sleep(10000);
            }
        } while ((response != null && response.statusCode() == 302) || !success);

        return response.body();
    }
}
