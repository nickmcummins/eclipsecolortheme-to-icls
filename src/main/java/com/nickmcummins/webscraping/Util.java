package com.nickmcummins.webscraping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Util {
    public static String get(String url) throws IOException, InterruptedException {
        HttpClient requests = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        do {
            if (response != null)
                url = response.headers().allValues("location").get(0);
            response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());
        } while (response.statusCode() == 302);

        return response.body();
    }
}
