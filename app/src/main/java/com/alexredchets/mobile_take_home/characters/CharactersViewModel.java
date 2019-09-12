package com.alexredchets.mobile_take_home.characters;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.alexredchets.mobile_take_home.BuildConfig;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Character;
import com.alexredchets.mobile_take_home.models.Episode;
import com.alexredchets.mobile_take_home.models.Location;
import com.alexredchets.mobile_take_home.models.Origin;

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
import java.util.List;

class CharactersViewModel extends AndroidViewModel {

    private CharactersLiveData characters;
    private String path;

    CharactersViewModel(@NonNull Application application, String path) {
        super(application);
        this.path = path;
        characters = new CharactersLiveData(application);
    }

    LiveData<List<Character>> getCharacters() {
        return characters;
    }

    public class CharactersLiveData extends LiveData<List<Character>> {

        private final Context context;

        CharactersLiveData(Context context) {
            this.context = context;
            loadCharacters();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadCharacters() {
            new AsyncTask<Void, Void, String>() {
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
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        JSONArray characterArray = new JSONArray(s);

                        ArrayList<Character> characters = new ArrayList<>();

                        for (int i = 0; i < characterArray.length(); i++) {
                            Character character = new Character();
                            // getting main object
                            JSONObject objectCharacter = characterArray.getJSONObject(i);
                            character.setId(objectCharacter.getInt("id"));
                            character.setName(objectCharacter.getString("name"));
                            character.setStatus(objectCharacter.getString("status"));
                            character.setSpecies(objectCharacter.getString("species"));
                            character.setType(objectCharacter.getString("type"));
                            character.setGender(objectCharacter.getString("gender"));
                            character.setImage(objectCharacter.getString("image"));
                            character.setUrl(objectCharacter.getString("url"));
                            character.setCreated(objectCharacter.getString("created"));
                            // getting origin object
                            JSONObject originObject = objectCharacter.getJSONObject("origin");
                            Origin origin = new Origin();
                            origin.setName(originObject.getString("name"));
                            origin.setUrl(originObject.getString("url"));
                            character.setOrigin(origin);
                            // getting location object
                            JSONObject locationObject = objectCharacter.getJSONObject("location");
                            Location location = new Location();
                            location.setName(locationObject.getString("name"));
                            location.setUrl(locationObject.getString("url"));
                            character.setLocation(location);
                            // getting episode object
                            JSONArray episodeArray = objectCharacter.getJSONArray("episode");
                            List<String> episodes = new ArrayList<>();
                            for (int j = 0; j < episodeArray.length(); j ++) {
                                episodes.add(episodeArray.get(j).toString());
                            }
                            character.setEpisode(episodes);
                            characters.add(character);
                        }
                        setValue(characters);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
