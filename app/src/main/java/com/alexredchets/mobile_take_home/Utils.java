package com.alexredchets.mobile_take_home;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import com.alexredchets.mobile_take_home.models.Character;
import com.alexredchets.mobile_take_home.models.Episode;
import com.alexredchets.mobile_take_home.models.Location;
import com.alexredchets.mobile_take_home.models.Origin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final String EPISODE_KEY = "episode";
    public static final String CHARACTER_KEY = "character";

    public static String getCharacterUrl(List<String> characters) {
        StringBuilder stringBuilder = new StringBuilder("character/");
        for (String item: characters) {
            String[] arr = item.split("/");
            stringBuilder.append(",").append(arr[arr.length - 1]);
        }
        return stringBuilder.toString();
    }

    public static List<Episode> convertJsonToEpisodeList(String s) {
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
            return episodes;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Character> convertJsonToCharacterList (String s) {
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
            return characters;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
    }

    public static SpannableStringBuilder setBold(String string, CharacterInfo characterInfo) {
        SpannableStringBuilder sb = new SpannableStringBuilder(string);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        switch (characterInfo) {
            case NAME:
                sb.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                break;
            case STATUS:
            case GENDER:
            case ORIGIN:
                sb.setSpan(bss, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                break;
            case SPECIES:
                sb.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                break;
            case LOCATION:
                sb.setSpan(bss, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                break;
        }
        return sb;
    }

    public enum CharacterInfo {
        NAME,
        STATUS,
        SPECIES,
        GENDER,
        ORIGIN,
        LOCATION
    }
}