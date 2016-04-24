package com.dma.yartists.util;

import org.junit.Test;

import static org.junit.Assert.*;


public class UtilsTest {

    @Test
    public void testPluralize() throws Exception {
        String[] albums = {"альбом","альбома","альбомов"};
        assertEquals(albums[0],albums[new Utils().pluralize(1,0,1,2)]);
        assertEquals(albums[1],albums[new Utils().pluralize(2,0,1,2)]);
        assertEquals(albums[2],albums[new Utils().pluralize(50,0,1,2)]);
        String[] tracks = {"песня","песни","песен"};
        assertEquals(tracks[0],tracks[new Utils().pluralize(1,0,1,2)]);
        assertEquals(tracks[1],tracks[new Utils().pluralize(2,0,1,2)]);
        assertEquals(tracks[2],tracks[new Utils().pluralize(50,0,1,2)]);
        //Eng
        String[] albumsEng = {"album","albums","albums"};
        assertEquals(albums[0],albums[new Utils().pluralize(1,0,1,2)]);
        assertEquals(albums[1],albums[new Utils().pluralize(2,0,1,2)]);
        assertEquals(albums[2],albums[new Utils().pluralize(50,0,1,2)]);
        String[] tracksEng = {"song","songs","songs"};
        assertEquals(tracks[0],tracks[new Utils().pluralize(1,0,1,2)]);
        assertEquals(tracks[1],tracks[new Utils().pluralize(2,0,1,2)]);
        assertEquals(tracks[2],tracks[new Utils().pluralize(50,0,1,2)]);
    }
}