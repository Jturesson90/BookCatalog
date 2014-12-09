package com.jesperturesson.booksearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerConnection {

    public static final String TAG = "ServerConnection";
    HttpURLConnection connection;

    public ServerConnection() {

    }

    public boolean jsonGet(final String url, Parser parser) {

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            String message = connection.getResponseMessage();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                parser.parse(input);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }

    public static String readStream(InputStream input) {

        String textToReturn = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            textToReturn = stringBuilder.toString();
        } catch (Exception e) {
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return textToReturn;
    }
}