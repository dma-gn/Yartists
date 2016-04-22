package com.dma.yartists.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    private long id;
    private String name;
    private String[] genres;
    private String link;
    private int tracks;
    private int albums;
    private String description;
    private Cover cover;

    public Artist() {
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        genres = in.createStringArray();
        link = in.readString();
        tracks = in.readInt();
        albums = in.readInt();
        description = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeStringArray(genres);
        dest.writeString(link);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(description);
    }

    public static class Cover {
        private String small;
        private String big;

        private Cover(){}

        public Cover(String small, String big) {
            this.small = small;
            this.big = big;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getBig() {
            return big;
        }

        public void setBig(String big) {
            this.big = big;
        }
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static String implode(String[] array){
        String result = "";
        for(int i=0;i<array.length;i++) {
            result += (i == array.length - 1) ? array[i] : array[i] + ", ";
        }
        return result;
    }

}
