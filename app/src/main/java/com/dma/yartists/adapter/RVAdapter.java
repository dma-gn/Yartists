package com.dma.yartists.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dma.yartists.R;
import com.dma.yartists.activity.ArtistDetailActivity;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.task.DownloadImageTask;
import com.dma.yartists.util.ApplicationConstants;
import com.dma.yartists.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ArtistViewHolder>  {

    public static class ArtistViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnClickListener, android.view.View.OnLongClickListener {

        CardView cv;
        TextView artistName;
        TextView artistGenres;
        ImageView artistPhoto;
        TextView artistDescription;
        Context ctx;

        int position;
        Artist artist;

        public void setPosition(int position) {
            this.position = position;
        }

        public void setArtist(Artist artist) {
            this.artist = artist;
        }

        ArtistViewHolder(View itemView, Context context) {
            super(itemView);
            ctx = context;
            cv = (CardView)itemView.findViewById(R.id.cv);
            artistName = (TextView)itemView.findViewById(R.id.artist_name);
            artistGenres = (TextView)itemView.findViewById(R.id.artist_genres);
            artistPhoto = (ImageView)itemView.findViewById(R.id.artist_photo);
            artistDescription = (TextView)itemView.findViewById(R.id.artist_albums_and_tracks);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, ArtistDetailActivity.class);
            intent.putExtra(ApplicationConstants.POSITION, position);

            ctx.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse(artist.getLink()));
            ctx.startActivity(viewIntent);
            return true;
        }
    }

    Context context;
    List<Artist> artists = new ArrayList<>();

    public void setArtists(List<Artist> artists) {

        this.artists = artists;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public RVAdapter(Context context){
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ArtistViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder artistViewHolder, int i) {
        artistViewHolder.setArtist(artists.get(i));
        artistViewHolder.setPosition(i);
        artistViewHolder.artistName.setText(artists.get(i).getName());

        String albums = artists.get(i).getAlbums() + " " +
                context.getString(new Utils().pluralize(artists.get(i).getAlbums(),
                        R.string.item_albums_singular,R.string.item_albums_plural,
                        R.string.item_albums_plural_perfect));

        String tracks = artists.get(i).getTracks() + " " +
                context.getString(new Utils().pluralize(artists.get(i).getTracks(),
                        R.string.item_tracks_singular,R.string.item_tracks_plural,
                        R.string.item_tracks_plural_perfect));

        artistViewHolder.artistDescription.setText(albums + ", " + tracks);
        artistViewHolder.artistGenres.setText(artists.get(i).implode(artists.get(i).getGenres()));

        String cacheFileName = ApplicationConstants.THUMB_PREFIX + String.valueOf(artists.get(i).getId());
        new DownloadImageTask(artistViewHolder.artistPhoto, cacheFileName).execute(artists.get(i).getCover().getSmall());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

}
