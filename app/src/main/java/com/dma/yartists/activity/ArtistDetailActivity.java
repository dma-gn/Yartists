package com.dma.yartists.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dma.yartists.R;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.task.DownloadImageTask;
import com.dma.yartists.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dma on 20.04.2016.
 */
public class ArtistDetailActivity extends Activity {

    private static final int LAYOUT = R.layout.activity_artist_details;
    private Toolbar toolbar;
    private List<Artist> artists = new ArrayList<Artist>();

    ImageView imageViewArtistPhoto;
    TextView textViewArtistAlbumsAndTracks;
    TextView textViewArtistGenres;
    TextView textViewArtistDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        artists = MainActivity.artists;
        Intent intent = getIntent();
        Artist artist = artists.get(intent.getIntExtra(ApplicationConstants.POSITION,0));
        initializeData(intent,artist);
        initializeToolBar(artist.getName());
    }

    private void initializeData(Intent intent, Artist artist) {
        textViewArtistAlbumsAndTracks = (TextView) findViewById(R.id.artist_albums_and_traks);
        textViewArtistGenres = (TextView) findViewById(R.id.artist_genres);
        textViewArtistDescription = (TextView) findViewById(R.id.artist_albums_and_tracks);
        imageViewArtistPhoto = (ImageView) findViewById(R.id.artist_photo);


        String albums = artist.getAlbums() + " " +
                getBaseContext().getString(R.string.item_albums);

        String tracks = artist.getTracks() + " " +
                getBaseContext().getString(R.string.item_tracks);

        textViewArtistAlbumsAndTracks.setText(albums + " - " + tracks);
        textViewArtistGenres.setText(artist.implode(artist.getGenres()));
        textViewArtistDescription.setText(artist.getDescription());
        new DownloadImageTask(imageViewArtistPhoto,String.valueOf(artist.getId())).execute(artist.getCover().getBig());

    }

    private void initializeToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}