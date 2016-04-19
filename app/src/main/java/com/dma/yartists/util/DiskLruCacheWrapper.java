package com.dma.yartists.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskLruCacheWrapper {
    private static final int VALUE_IDX = 0;
    private File cacheDir;
    private int version;
    private int valueCount;
    private long cacheDiskSize;

    public static DiskLruCache mDiskLruCache;
    public static final Object mDiskCacheLock = new Object();
    public static boolean mDiskCacheStarting = true;

    public class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                try {
                    mDiskLruCache = DiskLruCache.open(cacheDir, version, valueCount,
                            cacheDiskSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDiskCacheStarting = false;
                mDiskCacheLock.notifyAll();
            }
            return null;
        }
    }

    public DiskLruCacheWrapper(File dir, int appVersion, int valueCount, long maxSize) {
        this.cacheDir = dir;
        this.version = appVersion;
        this.valueCount = valueCount;
        this.cacheDiskSize = maxSize;
        new InitDiskCacheTask().execute(cacheDir);
//        mDiskLruCache = DiskLruCache.open(dir, appVersion, 2, maxSize);
    }

    public InputStream getInputStreamFromDiskCache(String key) throws IOException {
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskLruCache != null && mDiskLruCache.size() != 0 && mDiskLruCache.get("artists") != null) {
                return mDiskLruCache.get("artists").getInputStream(0);
            }
        }
        return null;
    }

    public void addInputStreamToDiskCache(String key, InputStream inputStream) throws IOException {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = null;
                try {
                    editor = mDiskLruCache.edit(key);
                    OutputStream out = new BufferedOutputStream(editor.newOutputStream(0), ApplicationConstants.MEM_CACHE_SIZE);
                    byte[] buf = new byte[1024];
                    int len;
                    while((len=inputStream.read(buf)) != -1){
                        out.write(buf,0,len);
                    }
                    out.close();
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                    editor.abort();
                }
            }
        }
    }

    public void addBitmapToCache(String key, Bitmap bitmap) throws IOException {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = null;
                try {
                    editor = mDiskLruCache.edit(key);
                    if ( editor == null ) {
                        return;
                    }
                    if( writeBitmapToFile( bitmap, editor ) ) {
                        mDiskLruCache.flush();
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                } catch (IOException e) {
                    try {
                        if ( editor != null ) {
                            editor.abort();
                        }
                    } catch (IOException ex) {
                        editor.abort();
                    }
                }
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(String key) throws IOException {
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mDiskLruCache != null && mDiskLruCache.size() != 0 && mDiskLruCache.get(key) != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(mDiskLruCache.get(key).getInputStream(0));
                addBitmapToCache(key, bitmap);
                return  bitmap;
            }
        }
        return null;
    }

    private boolean writeBitmapToFile( Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), ApplicationConstants.MEM_CACHE_SIZE);
            return bitmap.compress( Bitmap.CompressFormat.JPEG, 70, out );
        } finally {
            if ( out != null ) {
                out.close();
            }
        }
    }
}
