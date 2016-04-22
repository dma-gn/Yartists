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

    private ArtistsFragmentPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    private int rvPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        artists = MainActivity.artists;
        Intent intent = getIntent();
        rvPosition = intent.getIntExtra(ApplicationConstants.POSITION,0);
        initializeToolBar(artists.get(rvPosition).getName());
        initializeFragment();

    }

    private void initializeToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.recyclerView.scrollToPosition(rvPosition);
                finish();
            }
        });
    }

    private void initializeFragment() {
        pagerAdapter = new ArtistsFragmentPagerAdapter(getSupportFragmentManager(),artists);
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(rvPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(artists.get(position).getName());
                rvPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}