package com.alexredchets.mobile_take_home.episodes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.alexredchets.mobile_take_home.BuildConfig;
import com.alexredchets.mobile_take_home.Loader;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Episode;

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
                    return Loader.load(BuildConfig.BASE_URL + "episode/");
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
