
package com.alexredchets.mobile_take_home.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Character implements Parcelable {

    private String created;
    private List<String> episode;
    private String gender;
    private int id;
    private String image;
    private Location location;
    private String name;
    private Origin origin;
    private String species;
    private String status;
    private String type;
    private String url;

    public Character() {
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<String> getEpisode() {
        return episode;
    }

    public void setEpisode(List<String> episode) {
        this.episode = episode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.created);
        dest.writeStringList(this.episode);
        dest.writeString(this.gender);
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.name);
        dest.writeParcelable(this.origin, flags);
        dest.writeString(this.species);
        dest.writeString(this.status);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    private Character(Parcel in) {
        this.created = in.readString();
        this.episode = in.createStringArrayList();
        this.gender = in.readString();
        this.id = in.readInt();
        this.image = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.name = in.readString();
        this.origin = in.readParcelable(Origin.class.getClassLoader());
        this.species = in.readString();
        this.status = in.readString();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel source) {
            return new Character(source);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };
}
