package com.dma.yartists.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
/*
* Класс с полезными методами
* */
public class Utils {

    public Utils() {
    }

    //Выбор нужной формы существительного в зависимости количества {1 песня, 2 песни, 35 песен}
    public int pluralize(int number, int firstForm, int secondForm, int thirdForm) {
        int n = Math.abs(number) % 100;
        int m = number % 10;
        if (n > 10 && n < 20) return thirdForm;
        if (m > 1 && m < 5) return secondForm;
        if (m == 1) return firstForm;
        return thirdForm;
    }

    //Получение папки с кешем
    public static File getDiskCacheDir(Context context, String folderName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
                        context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + folderName);
    }
}
