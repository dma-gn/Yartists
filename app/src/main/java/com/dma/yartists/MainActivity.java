package com.dma.yartists;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.dma.yartists.adapter.RVAdapter;
import com.dma.yartists.dto.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private RVAdapter adapter;
    private RecyclerView recyclerView;
    private Toast toast;

    public static List<Artist> artists = new ArrayList<Artist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeToolBar();

        initializeRecyclerView();
        initializeAdapter();

        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
    }

    private void initializeToolBar() {
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.artists);
    }

    private void initializeRecyclerView() {
        recyclerView =(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }

    private void initializeAdapter(){
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        new ArtistAsyncTask().execute();
    }

    public class ArtistAsyncTask extends AsyncTask<Void, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(Void... params) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Artist[] list = new Artist[]{};

                list = mapper.readValue(
                        new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json").openStream(),
                        Artist[].class);

                artists = Arrays.asList(list);
            }catch (IOException e){
                toast.setText(e.getMessage());
                toast.show();
                e.printStackTrace();
            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            adapter.notifyItemRangeChanged(0, adapter.getArtists().size());
            adapter.setArtists(artists);
            adapter.notifyDataSetChanged();
        }
    }
}
