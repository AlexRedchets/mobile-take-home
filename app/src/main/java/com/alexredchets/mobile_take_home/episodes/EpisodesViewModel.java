package com.alexredchets.mobile_take_home.episodes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.alexredchets.mobile_take_home.BuildConfig;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Episode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EpisodesViewModel extends AndroidViewModel {

    private EpisodesLiveData episodes;
    private final MutableLiveData<Boolean> isItLoading = new MutableLiveData<>();

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        episodes = new EpisodesLiveData();
    }

    LiveData<List<Episode>> getEpisodes() {
        return episodes;
    }
    LiveData<Boolean> getIsLoading() {
        return isItLoading;
    }

    public class EpisodesLiveData extends LiveData<List<Episode>> {

        EpisodesLiveData() {
            isItLoading.setValue(true);
            loadEpisodes();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadEpisodes() {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    BufferedReader reader = null;
                    try {
                        URL myUrl = new URL(BuildConfig.BASE_URL + "episode/");
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
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s != null) {
                        setValue(Utils.convertJsonToEpisodeList(s));
                    } else {
                        setValue(null);
                    }
                    isItLoading.setValue(false);
                }
            }.execute();
        }
    }
}
