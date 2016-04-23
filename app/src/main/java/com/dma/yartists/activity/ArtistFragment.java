package com.dma.yartists.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dma.yartists.R;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.task.DownloadImageTask;
import com.dma.yartists.util.ApplicationConstants;
import com.dma.yartists.util.Utils;

public class ArtistFragment extends Fragment {

    public ArtistFragment() {
    }

    public static ArtistFragment newInstance(Artist artist) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putParcelable(ApplicationConstants.ARTIST_FRAGMENT_KEY,artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Artist artist = getArguments().getParcelable(ApplicationConstants.ARTIST_FRAGMENT_KEY);
        View rootView = inflater.inflate(R.layout.fragment_artist_details, container, false);

        ImageView imageViewArtistPhoto = (ImageView) rootView.findViewById(R.id.artist_photo);
        TextView textViewArtistAlbumsAndTracks = (TextView) rootView.findViewById(R.id.artist_albums_and_traks);
        TextView textViewArtistGenres  = (TextView) rootView.findViewById(R.id.artist_genres);
        TextView textViewArtistDescription = (TextView) rootView.findViewById(R.id.artist_description);


        String albums = artist.getAlbums() + " " +
                this.getString(new Utils().pluralize(artist.getAlbums(),
                        R.string.item_albums_singular,R.string.item_albums_plural,
                        R.string.item_albums_plural_perfect));

        String tracks = artist.getTracks() + " " +
                this.getString(new Utils().pluralize(artist.getTracks(),
                        R.string.item_tracks_singular,R.string.item_tracks_plural,
                        R.string.item_tracks_plural_perfect));

        textViewArtistAlbumsAndTracks.setText(albums + " • " + tracks);
        textViewArtistGenres.setText(artist.implode(artist.getGenres()));
        textViewArtistDescription.setText(artist.getDescription());
        new DownloadImageTask(getContext(), imageViewArtistPhoto,String.valueOf(artist.getId())).execute(artist.getCover().getBig());

        return rootView;
    }
}