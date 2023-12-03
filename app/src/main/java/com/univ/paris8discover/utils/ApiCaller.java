package com.univ.paris8discover.utils;

import com.univ.paris8discover.R;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ApiCaller extends AsyncTask<String, Void, String>{

    private String result;

    public ApiCaller() {

    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length < 2) {
            Log.e("ApiRequest", "Insufficient parameters");
            return null;
        }
        try {
            String apiUrl = params[0];
            String requestBody = params[1];


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input/output streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Write the request body to the connection
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(requestBody);
                wr.flush();
            }

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response from the server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Return the response
                result = response.toString();
            }

        } catch (Exception e) {
            Log.e("ApiRequest", "Error: " + e.getMessage());
            return null;
        }
        return result;
    }

    public String getResult() {
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result (e.g., update UI)
        if (result != null) {
            Log.d("ApiRequest", "Response: " + result);
        } else {
            Log.e("ApiRequest", "Request failed");
        }
    }


}
