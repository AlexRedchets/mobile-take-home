package com.alexredchets.mobile_take_home.episodes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.alexredchets.mobile_take_home.BuildConfig;
import com.alexredchets.mobile_take_home.models.Episode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EpisodesViewModel extends AndroidViewModel {

    private EpisodesLiveData episodes;

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        episodes = new EpisodesLiveData(application);
    }

    LiveData<List<Episode>> getEpisodes() {
        return episodes;
    }

    public class EpisodesLiveData extends LiveData<List<Episode>> {

        private final Context context;

        EpisodesLiveData(Context context) {
            this.context = context;
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

                    try {
                        JSONObject responseObject = new JSONObject(s);
                        JSONArray episodeArray = responseObject.getJSONArray("results");

                        ArrayList<Episode> episodes = new ArrayList<>();

                        for (int i = 0; i < episodeArray.length(); i++) {
                            Episode episode = new Episode();
                            JSONObject objectEpisode = episodeArray.getJSONObject(i);
                            episode.setId(objectEpisode.getInt("id"));
                            episode.setName(objectEpisode.getString("name"));
                            episode.setAirDate(objectEpisode.getString("air_date"));
                            episode.setEpisode(objectEpisode.getString("episode"));
                            episode.setUrl(objectEpisode.getString("url"));
                            episode.setCreated(objectEpisode.getString("created"));
                            JSONArray charactersArray = objectEpisode.getJSONArray("characters");
                            String[] characters = new String[charactersArray.length()];
                            for (int j = 0; j < charactersArray.length(); j ++) {
                                characters[j] = charactersArray.getString(j);
                            }
                            episode.setCharacters(Arrays.asList(characters));
                            episodes.add(episode);
                        }
                        setValue(episodes);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
