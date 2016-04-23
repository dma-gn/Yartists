package com.dma.yartists.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dma.yartists.R;
import com.dma.yartists.activity.MainActivity;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtistApi {

    public static boolean isConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public List<Artist> getArtists() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Artist[] list = new Artist[]{};
        InputStream artistsInputStream = null;

        artistsInputStream = getArtistInputStream();
        if(artistsInputStream != null){
            list = mapper.readValue(artistsInputStream, Artist[].class);
        }

        return Arrays.asList(list);
    }

    private InputStream getArtistInputStream() throws IOException{
        InputStream artistsInputStream = MainActivity.diskLruCacheWrapper.getInputStreamFromDiskCache("artists");
        if(artistsInputStream == null){
            artistsInputStream = new URL(ApplicationConstants.URL_ARTISTS).openStream();
            MainActivity.diskLruCacheWrapper.addInputStreamToDiskCache("artists",
                    new URL(ApplicationConstants.URL_ARTISTS).openStream());
        }
        return artistsInputStream;
    }
}
