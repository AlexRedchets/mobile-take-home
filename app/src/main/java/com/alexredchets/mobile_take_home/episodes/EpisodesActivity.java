package com.alexredchets.mobile_take_home.episodes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.alexredchets.mobile_take_home.ItemClickListener;
import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.characters.CharactersActivity;
import com.alexredchets.mobile_take_home.models.Episode;

import java.util.List;

import static com.alexredchets.mobile_take_home.Utils.EPISODE_KEY;

public class EpisodesActivity extends AppCompatActivity implements ItemClickListener<Episode> {

    private EpisodesAdapter episodesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        getSupportActionBar().setTitle("Episodes");

        // init ViewModel
        EpisodesViewModel episodesViewModel = ViewModelProviders.of(this).get(EpisodesViewModel.class);

        // init Recycler View and Adapter
        RecyclerView recyclerView = findViewById(R.id.episodesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        episodesAdapter = new EpisodesAdapter(this);
        recyclerView.setAdapter(episodesAdapter);

        // load episodes and observe changes
        episodesViewModel.getEpisodes().observe(this, new Observer<List<Episode>>() {
            ConstraintLayout errorLayout = findViewById(R.id.episodesErrorLayout);
            @Override
            public void onChanged(@Nullable List<Episode> episodes) {
                if (episodes != null) {
                    errorLayout.setVisibility(View.GONE);
                    episodesAdapter.updateAdapter(episodes);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        episodesViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            final ConstraintLayout progressBar = findViewById(R.id.episodesSpinnerLayout);
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
    public void onItemClicked(Episode item) {
        Intent intent = new Intent(this, CharactersActivity.class);
        intent.putExtra(EPISODE_KEY, item);
        startActivity(intent);
    }
}
