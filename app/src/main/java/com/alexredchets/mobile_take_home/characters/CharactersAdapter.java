package com.alexredchets.mobile_take_home.characters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexredchets.mobile_take_home.ItemClickListener;
import com.alexredchets.mobile_take_home.R;
import com.alexredchets.mobile_take_home.episodes.EpisodesAdapter;
import com.alexredchets.mobile_take_home.models.Character;
import com.alexredchets.mobile_take_home.models.Episode;

import java.util.ArrayList;
import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.ViewHolder> {

    private List<Character> characters = new ArrayList<>();
    ItemClickListener<Character> listener;

    CharactersAdapter(ItemClickListener<Character> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharactersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Character character = characters.get(position);
        if (character.getStatus().equalsIgnoreCase("Alive")) {
            viewHolder.characterName.setTextColor(
                    ContextCompat.getColor(viewHolder.characterName.getContext(), R.color.text_black));
        } else {
            viewHolder.characterName.setTextColor(
                    ContextCompat.getColor(viewHolder.characterName.getContext(), R.color.text_gray));
        }
        viewHolder.characterName.setText(character.getName());
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void updateAdapter(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView characterName;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            characterName = view.findViewById(R.id.characterName);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClicked(characters.get(getAdapterPosition()));
        }
    }

}
