package com.alexredchets.mobile_take_home.episodes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        // init ViewModel
        EpisodesViewModel viewModel = ViewModelProviders.of(this).get(EpisodesViewModel.class);

        // init Recycler View and Adapter
        RecyclerView recyclerView = findViewById(R.id.episodesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        episodesAdapter = new EpisodesAdapter(this);
        recyclerView.setAdapter(episodesAdapter);

        // load episodes and observe changes
        viewModel.getEpisodes().observe(this, new Observer<List<Episode>>() {
            @Override
            public void onChanged(@Nullable List<Episode> episodes) {
                episodesAdapter.updateAdapter(episodes);
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
