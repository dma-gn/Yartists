package com.dma.yartists.util;

public class ApplicationConstants {

    public static final int VERSION = 1;

    public static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024*4);
    public static final int MEM_CACHE_SIZE = MAX_MEMORY / 4;
    public static final Long CACHE_DISK_SIZE = 1024 * 1024 * 50L;

    public static final String THUMB_PREFIX = "thumb_";

    public static final String URL_ARTISTS = "http://download.cdn.yandex.net/mobilization-2016/artists.json";

    public static final String POSITION = "position";
    public static final String ARTIST_FRAGMENT_KEY = "artist";
}
