package com.dma.yartists.dto;

public class Artist {
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
