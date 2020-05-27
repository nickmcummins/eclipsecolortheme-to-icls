package com.nickmcummins.webscraping.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.nickmcummins.webscraping.Util.print;
import static java.lang.Thread.sleep;

public class HttpUtil {
    private static final int MAX_RETRIES = 5;
    private static final List<LocalDateTime> CONNECTION_REFUSED_TIMESTAMPS = new ArrayList<>();
    private static final Duration CONNECT_REFUSED_EXPIRATION = Duration.ofMinutes(10);
    private static final int SECONDS_IN_MS = 1000;

    public static String get(String url) throws InterruptedException, CannotDownloadException {
        HttpClient requests = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        boolean success = false;
        int tries = 0;

        do {
            removeExpiredConnectRefusedTimestamps();
            if (!CONNECTION_REFUSED_TIMESTAMPS.isEmpty()) {
                int seconds = 60 + 30 * (CONNECTION_REFUSED_TIMESTAMPS.size()-1);
                print("\tSleeping %d seconds because of %d connection refused responses within the past 10 minutes: %s", seconds, CONNECTION_REFUSED_TIMESTAMPS.size(), CONNECTION_REFUSED_TIMESTAMPS.toString());
                sleep(seconds * SECONDS_IN_MS);
            }

            if (response != null && response.statusCode() == 302)
                url = response.headers().allValues("location").get(0);
            try {
                tries += 1;
                response = requests.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200)
                    success = true;
                else if (response.statusCode() == 404) {
                    System.out.println(String.format("\tReceived 404 attempting to download %s; skipping", url));
                    throw new CannotDownloadException();
                }
            } catch (IOException ce) {
                if (ce instanceof ConnectException || ce.getMessage().equals("Received RST_STREAM: Internal error")) {
                    if (ce.getMessage().equals("Connection refused"))
                        CONNECTION_REFUSED_TIMESTAMPS.add(LocalDateTime.now());
                    else {
                        System.out.println(String.format("\tConnection exception when attempting to download %s. Sleeping %d seconds.", url, 10 * tries));
                        sleep(10000 * tries);
                    }
                    System.out.println("\t" + ce);
                }
            }
        } while ((response != null && response.statusCode() == 302) || (!success && tries < MAX_RETRIES));
        if (response == null)
            throw new CannotDownloadException();

        return response.body();
    }

    private static void removeExpiredConnectRefusedTimestamps() {
        if (CONNECTION_REFUSED_TIMESTAMPS.isEmpty())
            return;

        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> expired = CONNECTION_REFUSED_TIMESTAMPS.stream()
                .filter(timestamp -> Duration.between(timestamp, now).compareTo(CONNECT_REFUSED_EXPIRATION) > 0)
                .collect(Collectors.toList());
        print("\tRemoving %d timestamps from the connect timeout list", expired.size());
        CONNECTION_REFUSED_TIMESTAMPS.removeAll(expired);

    }
}
