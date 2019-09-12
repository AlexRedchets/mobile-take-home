package com.alexredchets.mobile_take_home.episodes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexredchets.mobile_take_home.ItemClickListener;
import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.models.Episode;
import java.util.ArrayList;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private List<Episode> episodes = new ArrayList<>();
    ItemClickListener<Episode> listener;

    EpisodesAdapter(ItemClickListener<Episode> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Episode episode = episodes.get(position);
        viewHolder.episodeName.setText(viewHolder.episodeName.getContext().getString(
                R.string.episode_name, episode.getEpisode(), episode.getName()));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void updateAdapter(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView episodeName;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            episodeName = view.findViewById(R.id.episodeTitle);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClicked(episodes.get(getAdapterPosition()));
        }
    }
}
