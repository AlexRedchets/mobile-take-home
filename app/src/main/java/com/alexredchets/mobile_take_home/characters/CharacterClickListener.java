package com.alexredchets.mobile_take_home.characters;

import com.alexredchets.mobile_take_home.models.Character;

public interface CharacterClickListener {
    void onItemClicked(Character item);
    void onStatusClicked(int itemPosition);
}
