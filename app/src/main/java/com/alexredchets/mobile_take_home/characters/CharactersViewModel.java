package com.alexredchets.mobile_take_home.characters;

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
import com.alexredchets.mobile_take_home.models.Character;

import java.util.List;

class CharactersViewModel extends AndroidViewModel {

    private CharactersLiveData characters;
    private String path;
    private final MutableLiveData<Boolean> isItLoading = new MutableLiveData<>();

    CharactersViewModel(@NonNull Application application, String path) {
        super(application);
        this.path = path;
        characters = new CharactersLiveData();
    }

    LiveData<List<Character>> getCharacters() {
        return characters;
    }
    LiveData<Boolean> getIsLoading() {
        return isItLoading;
    }

    public class CharactersLiveData extends LiveData<List<Character>> {

        CharactersLiveData() {
            isItLoading.setValue(true);
            loadCharacters();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadCharacters() {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    return Loader.load(BuildConfig.BASE_URL + path);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s != null) {
                        setValue(Utils.convertJsonToCharacterList(s));
                    } else {
                        setValue(null);
                    }
                    isItLoading.setValue(false);
                }
            }.execute();
        }
    }
}
