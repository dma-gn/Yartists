package com.dma.yartists.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Utils {

    public Utils() {
    }

    public int pluralize(int number, int firstForm, int secondForm, int thirdForm) {
        int n = Math.abs(number) % 100;
        int m = number % 10;
        if (n > 10 && n < 20) return thirdForm;
        if (m > 1 && m < 5) return secondForm;
        if (m == 1) return firstForm;
        return thirdForm;
    }

    public static File getDiskCacheDir(Context context, String folderName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
                        context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + folderName);
    }
}
