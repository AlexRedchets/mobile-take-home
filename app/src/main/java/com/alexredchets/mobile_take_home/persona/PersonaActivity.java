package com.alexredchets.mobile_take_home.persona;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Character;

import static com.alexredchets.mobile_take_home.Utils.CHARACTER_KEY;

public class PersonaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        Character character = getIntent().getParcelableExtra(CHARACTER_KEY);
        setValues(character);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(character.getName());
        }
    }

    private void setValues(final Character character) {
        final ImageView image = findViewById(R.id.personaImage);

        // init View Model
        PersonaViewModel personaViewModel = ViewModelProviders.of(
                this, new ViewModelProvider.Factory() {
                    @SuppressWarnings("unchecked")
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new PersonaViewModel(getApplication(), character.getImage());
                    }
                }).get(PersonaViewModel.class);
        // load image and observe changes
        personaViewModel.getImage().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    image.setImageBitmap(Utils.getRoundedBitmap(bitmap));
                }
            }
        });

        TextView name = findViewById(R.id.personaName);
        TextView status = findViewById(R.id.personaStatus);
        TextView species = findViewById(R.id.personaSpecies);
        TextView gender = findViewById(R.id.personaGender);
        TextView origin = findViewById(R.id.personaOrigin);
        TextView location = findViewById(R.id.personaLocation);
        name.setText(Utils.setBold(
                getResources().getString(R.string.name, character.getName()), Utils.CharacterInfo.NAME));
        status.setText(Utils.setBold(
                getResources().getString(R.string.status, character.getStatus()), Utils.CharacterInfo.STATUS));
        species.setText(Utils.setBold(
                getResources().getString(R.string.species, character.getSpecies()), Utils.CharacterInfo.SPECIES));
        gender.setText(Utils.setBold(
                getResources().getString(R.string.gender, character.getGender()), Utils.CharacterInfo.GENDER));
        origin.setText(Utils.setBold(
                getResources().getString(R.string.origin, character.getOrigin().getName()), Utils.CharacterInfo.ORIGIN));
        location.setText(Utils.setBold(
                getString(R.string.location, character.getLocation().getName()), Utils.CharacterInfo.LOCATION));
    }
}
