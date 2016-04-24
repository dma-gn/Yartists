package com.dma.yartists.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dma.yartists.R;
import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ArtistDetailActivityTest extends ActivityInstrumentationTestCase2<ArtistDetailActivity> {

    private Activity mActivity;
    private ViewPager viewPager;
    private TextView albumsAndTracks;
    private TextView textViewArtistGenres;
    private TextView textViewArtistDescription;

    public ArtistDetailActivityTest() {
        super(ArtistDetailActivity.class);
    }


    @Before
    public void setUp() throws Exception {
        super.setUp();

        Intent startIntent = new Intent();
        startIntent.putExtra(ApplicationConstants.POSITION, 0);
        ArrayList<Artist> artistArrayList = new ArrayList<>(getArtists());
        startIntent.putParcelableArrayListExtra(ApplicationConstants.ARTISTS, artistArrayList);
        setActivityIntent(startIntent);
        mActivity = getActivity();
        viewPager = (ViewPager) mActivity.findViewById(R.id.container);
        albumsAndTracks = (TextView) viewPager.findViewById(R.id.artist_albums_and_traks);
        textViewArtistGenres  = (TextView) viewPager.findViewById(R.id.artist_genres);
        textViewArtistDescription = (TextView) viewPager.findViewById(R.id.artist_description);

    }

    @Test
    public void toolbarTitleAssert() {
        Toolbar toolbar = (Toolbar)mActivity.findViewById(R.id.toolbar);
        assertEquals("Tove Lo", toolbar.getTitle());
    }

    @Test
    public void viewsNotNullAssert() {
        assertNotNull(textViewArtistGenres);
        assertNotNull(albumsAndTracks);
        assertNotNull(textViewArtistDescription);

    }

    @Test
    public void viewsDataAssert(){
        //т.к. получаем 2 фрагмента n и n+1
        assertEquals("152 альбома • 256 песен", albumsAndTracks.getText());
        assertEquals("rnb, pop, rap", textViewArtistGenres.getText());
    }

    private List<Artist> getArtists() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Artist[] list = mapper.readValue(new URL(ApplicationConstants.URL_ARTISTS).openStream(), Artist[].class);
        return Arrays.asList(list);
    }

}