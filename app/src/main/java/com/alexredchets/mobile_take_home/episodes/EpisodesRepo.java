package com.alexredchets.mobile_take_home.episodes;

import android.util.Log;

import com.alexredchets.mobile_take_home.DataLoader;
import com.alexredchets.mobile_take_home.LoadListener;
import com.alexredchets.mobile_take_home.models.Episode;

import java.util.List;

public class EpisodesRepo implements LoadListener {

    /*List<Episode> loadEpisodes() {
        DataLoader loader = new DataLoader("episode/", this);
        loader.execute();
    }*/

    @Override
    public void onDataReceived(String data) {
        Log.d("", "onDataReceived: ");
    }

    @Override
    public void onError() {

    }
}
