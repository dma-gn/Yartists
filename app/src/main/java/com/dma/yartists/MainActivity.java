package com.dma.yartists;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class MainActivity extends Activity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeToolBar();
    }

    private void initializeToolBar() {
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.artists);
    }
}
