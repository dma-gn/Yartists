package com.dma.yartists;

import com.dma.yartists.dto.Artist;
import com.dma.yartists.util.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArtistTest {

    @Test
    public void ArtistsFill() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Artist[] list = mapper.readValue(new URL(ApplicationConstants.URL_ARTISTS).openStream(), Artist[].class);
        assertEquals(list.length, 317);
        for (Artist artist : list) {
            assertNotNull(artist.getId());
            assertNotNull(artist.getName());
            assertNotNull(artist.getGenres());
            assertNotNull(artist.implode(artist.getGenres()));
            assertNotNull(artist.getTracks());
            assertNotNull(artist.getAlbums());
            assertNotNull(artist.getDescription());
            assertNotNull(artist.getCover());
            assertNotNull(artist.getCover().getBig());
            assertNotNull(artist.getCover().getSmall());
        }
    }
}