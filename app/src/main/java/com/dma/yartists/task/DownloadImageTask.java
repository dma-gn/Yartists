package com.dma.yartists.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.dma.yartists.R;
import com.dma.yartists.activity.MainActivity;
import com.dma.yartists.util.ApplicationConstants;
import com.dma.yartists.util.DiskLruCacheWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private Context ctx;
    private ImageView bmImage;
    private String name;
    private final WeakReference<ImageView> imageViewReference;

    private static LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(ApplicationConstants.MEM_CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    private static DiskLruCacheWrapper diskLruCacheWrapper = MainActivity.diskLruCacheWrapper;

    public DownloadImageTask(Context ctx, ImageView bmImage, String name) {
        imageViewReference = new WeakReference<ImageView>(bmImage);
        this.bmImage = bmImage;
        this.name = name;
        this.ctx = ctx;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon = null;
        try {
            mIcon = getBitmapFromDiskCache(name);
            if(mIcon == null) {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
                addBitmapToCache(String.valueOf(name), mIcon);
            }
        }catch (IOException e){
            Handler handler =  new Handler(ctx.getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                    Toast.makeText(ctx, ctx.getString(R.string.can_not_download_photo),Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (imageViewReference != null && result != null) {
            this.bmImage = (ImageView) imageViewReference.get();
            if (bmImage != null) {
                bmImage.setImageBitmap(result);
            }
        }
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        bmImage.startAnimation(animation);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mLruCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    public void addBitmapToCache(String key, Bitmap bitmap) throws IOException {
        if (getBitmapFromMemCache(key) == null) {
            addBitmapToMemoryCache(key, bitmap);
        }
        diskLruCacheWrapper.addBitmapToCache(key,bitmap);
    }

    public Bitmap getBitmapFromDiskCache(String key) throws IOException {
        return (getBitmapFromMemCache(key) == null) ?
                diskLruCacheWrapper.getBitmapFromDiskCache(key) : getBitmapFromMemCache(key);
    }
}