
package com.alexredchets.mobile_take_home.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Episode implements Parcelable {

    private String airDate;
    private List<String> characters;
    private String created;
    private String episode;
    private int id;
    private String name;
    private String url;

    public Episode() {
    }

    private Episode(Parcel in) {
        airDate = in.readString();
        characters = in.createStringArrayList();
        created = in.readString();
        episode = in.readString();
        id = in.readInt();
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(airDate);
        parcel.writeStringList(characters);
        parcel.writeString(created);
        parcel.writeString(episode);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
