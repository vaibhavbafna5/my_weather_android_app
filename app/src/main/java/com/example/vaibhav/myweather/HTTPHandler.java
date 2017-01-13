package com.example.vaibhav.myweather;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vaibhav on 11/22/16.
 */

public class HTTPHandler {

    private static final String TAG = HTTPHandler.class.getSimpleName();

    public HTTPHandler() {

    }

    public String makeHttpServiceRequest(String reqUrl) {
        String response = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            response = convertStreamToString(inputStream);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException");
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }

        return response;
    }

    private String convertStreamToString(InputStream in) {

        String output = "";

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }

        output = stringBuilder.toString();
        return output;
    }

}
