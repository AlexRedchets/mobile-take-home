package com.alexredchets.mobile_take_home.characters;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexredchets.mobile_take_home.ItemClickListener;
import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Character;
import com.alexredchets.mobile_take_home.models.Episode;
import com.alexredchets.mobile_take_home.persona.PersonaActivity;

import java.util.List;

import static com.alexredchets.mobile_take_home.Utils.CHARACTER_KEY;
import static com.alexredchets.mobile_take_home.Utils.EPISODE_KEY;

public class CharactersActivity extends AppCompatActivity implements ItemClickListener<Character> {

    private CharactersAdapter charactersAdapter;
    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        episode = getIntent().getParcelableExtra(EPISODE_KEY);

        // init View Model
        CharactersViewModel charactersViewModel = ViewModelProviders.of(
                this, new ViewModelProvider.Factory() {
                    @SuppressWarnings("unchecked")
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new CharactersViewModel(getApplication(), Utils.getCharacterUrl(episode.getCharacters()));
                    }
                }).get(CharactersViewModel.class);

        // init Recycler View and Adapter
        RecyclerView recyclerView = findViewById(R.id.charactersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        charactersAdapter = new CharactersAdapter(this);
        recyclerView.setAdapter(charactersAdapter);

        // load characters and observe changes
        charactersViewModel.getCharacters().observe(this, new Observer<List<Character>>() {
            ConstraintLayout errorLayout = findViewById(R.id.charactersErrorLayout);
            @Override
            public void onChanged(@Nullable List<Character> characters) {
                if (characters != null) {
                    errorLayout.setVisibility(View.GONE);
                    charactersAdapter.updateAdapter(characters);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        charactersViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            final ConstraintLayout progressBar = findViewById(R.id.charactersSpinnerLayout);
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null || !aBoolean) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onItemClicked(Character item) {
        Intent intent = new Intent(this, PersonaActivity.class);
        intent.putExtra(CHARACTER_KEY, item);
        startActivity(intent);
    }
}
