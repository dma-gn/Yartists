package com.dma.yartists.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.dma.yartists.R;
import com.dma.yartists.adapter.RVAdapter;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;
import com.dma.yartists.util.DiskLruCacheWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.disklrucache.DiskLruCache;

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

    public static DiskLruCache mDiskLruCache;
    public static final Object mDiskCacheLock = new Object();
    public static boolean mDiskCacheStarting = true;
    public static DiskLruCacheWrapper diskLruCacheWrapper;

    public static List<Artist> artists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeDiskCache();
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
            try{
                ObjectMapper mapper = new ObjectMapper();
                Artist[] list = new Artist[]{};
                InputStream artistsInputStream = null;

                artistsInputStream = diskLruCacheWrapper.getInputStreamFromDiskCache("artists");
                if(artistsInputStream == null){
                    artistsInputStream = new URL(ApplicationConstants.URL_ARTISTS).openStream();
                    list = mapper.readValue(artistsInputStream, Artist[].class);
                    diskLruCacheWrapper.addInputStreamToDiskCache("artists",
                            new URL(ApplicationConstants.URL_ARTISTS).openStream());
                }else{
                    list = mapper.readValue(artistsInputStream, Artist[].class);
                }
                artistsInputStream.close();

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

    private void initializeDiskCache(){
        File cacheDir = getDiskCacheDir(this, "images");
        diskLruCacheWrapper = new DiskLruCacheWrapper(cacheDir, ApplicationConstants.VERSION, 1,
                Long.valueOf(ApplicationConstants.CACHE_DISK_SIZE));

    }

    public static File getDiskCacheDir(Context context, String folderName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
                        context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + folderName);
    }
}
