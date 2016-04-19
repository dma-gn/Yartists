package com.dma.yartists.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dma.yartists.R;
import com.dma.yartists.dto.Artist;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ArtistViewHolder>  {

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {

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
        ArtistViewHolder aph = new ArtistViewHolder(v,context);
        return aph;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder artistViewHolder, int i) {
        artistViewHolder.setArtist(artists.get(i));
        artistViewHolder.setPosition(i);
        artistViewHolder.artistName.setText(artists.get(i).getName());
        String albums = artists.get(i).getAlbums() + " " +
                context.getString(R.string.item_albums);

        String tracks = artists.get(i).getTracks() + " " +
                context.getString(R.string.item_tracks);
        artistViewHolder.artistDescription.setText(albums + ", " + tracks);
        artistViewHolder.artistGenres.setText(artists.get(i).implode(artists.get(i).getGenres()));

        new DownloadImageTask(artistViewHolder.artistPhoto).execute(artists.get(i).getCover().getSmall());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
