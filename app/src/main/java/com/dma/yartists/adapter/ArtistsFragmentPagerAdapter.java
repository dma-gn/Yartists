package com.dma.yartists.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dma.yartists.activity.ArtistFragment;
import com.dma.yartists.dto.Artist;

import java.util.List;

//Адаптер переключения страниц фрагментов
public class ArtistsFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Artist> artists;

    public ArtistsFragmentPagerAdapter(FragmentManager fm, List<Artist> artists) {
        super(fm);
        this.artists = artists;
    }

    @Override
    public Fragment getItem(int position) {
        //создаем инстанс фрагмента по позиции листа
        return ArtistFragment.newInstance(artists.get(position));
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return artists.get(position).getName();
    }
}