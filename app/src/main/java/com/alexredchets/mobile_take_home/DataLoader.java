package com.alexredchets.mobile_take_home;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends AsyncTask<Void, Void, String> {

    private String path;
    private LoadListener listener;

    public DataLoader(String path, LoadListener listener) {
        this.path = path;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        BufferedReader reader = null;

        try {

            URL myUrl = new URL(BuildConfig.BASE_URL + path);

            HttpURLConnection conn = (HttpURLConnection) myUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int statusCode = conn.getResponseCode();
            if (statusCode != 200){
                return null;
            }

            InputStream inputStream = conn.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

        if (data != null) {
            listener.onDataReceived(data);
        } else {
            listener.onError();
        }
    }
}
