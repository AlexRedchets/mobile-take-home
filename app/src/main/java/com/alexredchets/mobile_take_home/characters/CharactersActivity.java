package com.alexredchets.mobile_take_home.characters;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.Utils;
import com.alexredchets.mobile_take_home.models.Character;
import com.alexredchets.mobile_take_home.models.Episode;
import com.alexredchets.mobile_take_home.persona.PersonaActivity;

import java.util.List;

import static com.alexredchets.mobile_take_home.Utils.CHARACTER_KEY;
import static com.alexredchets.mobile_take_home.Utils.EPISODE_KEY;

public class CharactersActivity extends AppCompatActivity implements CharacterClickListener {

    private List<Character> characterList;
    private CharactersAdapter charactersAdapter;
    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Characters");
        }

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
                characterList = characters;
                if (characters != null) {
                    errorLayout.setVisibility(View.GONE);
                    charactersAdapter.updateAdapter(characters);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // observe loading changes
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

    @Override
    public void onStatusClicked(final int itemPosition) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.kill))
                .setMessage(getResources().getString(R.string.are_you_sure, characterList.get(itemPosition).getName()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        characterList.get(itemPosition).setStatus("Dead");
                        charactersAdapter.updateAdapter(characterList);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
