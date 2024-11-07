package org.example.bankapp;

import javafx.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnection {
    public static Pair<Integer, String> executeAPI(String requestType, String targetURL, String jsonInputString) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(jsonInputString);
            wr.close();

            //Get Response
            int responseCode = connection.getResponseCode();
            InputStream is;

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { // success
                is = connection.getInputStream();
            } else { // any non-200 response
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return new Pair<>(responseCode, response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(500, null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    public static Pair<Integer, String> executeAPI(String requestType, String targetURL) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.setDoOutput(true);

            //Get Response
            int responseCode = connection.getResponseCode();
            InputStream is;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { // success
                is = connection.getInputStream();
            } else { // any non-200 response
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new Pair<>(responseCode, response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(500, null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
