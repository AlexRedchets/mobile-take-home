package com.alexredchets.mobile_take_home;

import java.util.List;

public class Utils {

    public static final String EPISODE_KEY = "episode";

    public static String getCharacterUrl(List<String> characters) {
        StringBuilder stringBuilder = new StringBuilder("character/");
        for (String item: characters) {
            String[] arr = item.split("/");
            stringBuilder.append(",").append(arr[arr.length - 1]);
        }
        return stringBuilder.toString();
    }

}
