package com.alexredchets.mobile_take_home.persona;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.InputStream;
import java.net.URL;

public class PersonaViewModel extends AndroidViewModel {

    private ImageLiveData image;
    private String imageUrl;

    public PersonaViewModel(@NonNull Application application, String imageUrl) {
        super(application);
        this.imageUrl = imageUrl;
        image = new ImageLiveData();
    }

    LiveData<Bitmap> getImage() {
        return image;
    }

    public class ImageLiveData extends LiveData<Bitmap> {

        public ImageLiveData() {
            loadBitmap();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadBitmap() {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... voids) {
                    Bitmap bmp = null;
                    try {
                        InputStream in = new URL(imageUrl).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bmp;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);

                    if (bitmap != null) {
                        setValue(bitmap);
                    }
                }
            }.execute();
        }
    }
}
