package com.dma.yartists.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dma.yartists.R;
import com.dma.yartists.adapter.ArtistsRecyclerViewAdapter;
import com.dma.yartists.api.ArtistApi;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;
import com.dma.yartists.util.DiskLruCacheWrapper;
import com.dma.yartists.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


public class MainActivity extends AppCompatActivity {
    //чтобы не искать и можно было сразу найти главный макет
    private static final int LAYOUT = R.layout.activity_main;

    public static RecyclerView recyclerView;
    public static DiskLruCacheWrapper diskLruCacheWrapper;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewSwitcher viewSwitcher;
    private ProgressDialog loading;
    private Toast toast;
    private ArtistsRecyclerViewAdapter adapter;
    private List<Artist> artists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        //запускаем на экран загрузки прогресс диалог
        String loadingTitle = this.getBaseContext().getString(R.string.loading_title);
        String loadingContent = this.getBaseContext().getString(R.string.loading_content);
        loading = ProgressDialog.show(this,loadingTitle,loadingContent,false,false);

        //методы инициализации, описаны ниже
        initializeDiskCache();
        initializeToolBar();
        initializeRecyclerView();
        initializeAdapter();
        initializeSwipeRefresh();

        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
    }

    private void initializeToolBar() {
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.artists);
    }

    private void initializeRecyclerView() {
        recyclerView =(RecyclerView)findViewById(R.id.rv);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }

    private void initializeAdapter(){
        adapter = new ArtistsRecyclerViewAdapter(this);
        //"обворачиваем" адаптер анимацией устанавливаем время анимации 2 секунды
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(2000);
        recyclerView.setAdapter(alphaAdapter);
        loading.onStart();
        new ArtistAsyncTask().execute();
    }

    public class ArtistAsyncTask extends AsyncTask<Void, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(Void... params) {
            ArtistApi artistApi = new ArtistApi();
            //проверяем соединение, если его нет, выводим тост
            if(!artistApi.isConnect(getBaseContext())) {
                toast.setText(R.string.unable_to_connection);
                toast.show();
                return artists;
            }
            //получаем список исполнителей
            try {
                artists = artistApi.getArtists();
            } catch (ConnectException e) {
                toast.setText(R.string.connect_exception);
                toast.show();
            } catch (UnknownHostException e) {
                toast.setText(R.string.unknow_host_exception);
                toast.show();
            }catch (IOException e){
                toast.setText(R.string.connect_exception);
                toast.show();
            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            //Если список пустой и не выбран элемент с тексом(список пуст) выбираем его иначе если не выбран лист выбираем его
            if ((artists.isEmpty() || artists.size() == 0) &&
                    R.id.empty_text_view == viewSwitcher.getNextView().getId()) {
                viewSwitcher.showNext();
            }else if(R.id.rv == viewSwitcher.getNextView().getId()){
                    viewSwitcher.showNext();
            }
            adapter.notifyItemRangeChanged(0, adapter.getArtists().size());
            adapter.setArtists(artists);
            adapter.notifyDataSetChanged();
            //отключаем диалог
            loading.cancel();
            //убираем свайп
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initializeSwipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        //устанавливаем разные цвета свайпа
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_color_first,
                R.color.refresh_color_second,
                R.color.refresh_color_third,
                R.color.refresh_color_fourth);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    //чистим кеш
                    diskLruCacheWrapper.clearDiskCache();
                } catch (IOException e) {
                    toast.setText(R.string.clear_cache_exception);
                    toast.show();
                    e.printStackTrace();
                }
                //запускаем задачу по получению списка артистов
                new ArtistAsyncTask().execute();
            }
        });
    }

    private void initializeDiskCache(){
        //Получаем папку в которой будет храниться кеш
        File cacheDir = new Utils().getDiskCacheDir(this, "images");
        //Инициализируем в классе обертке кеш
        diskLruCacheWrapper = new DiskLruCacheWrapper(cacheDir, ApplicationConstants.VERSION, 1,
                ApplicationConstants.CACHE_DISK_SIZE);
    }

}
