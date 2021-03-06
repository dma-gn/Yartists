package com.dma.yartists.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dma.yartists.R;
import com.dma.yartists.adapter.ArtistsFragmentPagerAdapter;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_artist_details;
    private Toolbar toolbar;
    private List<Artist> artists = new ArrayList<>();

    private int rvPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Intent intent = getIntent();
        //получаем переданные намерения
        rvPosition = intent.getIntExtra(ApplicationConstants.POSITION,0);
        artists = intent.getParcelableArrayListExtra(ApplicationConstants.ARTISTS);
        initializeToolBar(artists.get(rvPosition).getName());
        initializeFragment();
    }

    private void initializeToolBar(String title) {
        //Инициализируем панель и подключаем слушатель на кнопку назад
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //передаем в главный экран позицию на которой закончили просмотр
                MainActivity.recyclerView.scrollToPosition(rvPosition);
                finish();
            }
        });
    }

    private void initializeFragment() {
        //инициализируем адаптер переключений страниц
        ArtistsFragmentPagerAdapter pagerAdapter = new ArtistsFragmentPagerAdapter(getSupportFragmentManager(),artists);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        //переключаем на страницу фрагмента, который мы нажали в экране выбора исполнителей
        viewPager.setCurrentItem(rvPosition);
        //подключаем слушатель переключения страниц фрагмента
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //меняем имя артиста в панели и перезаписываем позицию
                toolbar.setTitle(artists.get(position).getName());
                rvPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}