package com.app.exera.camera.utils.rx;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import rx.*;
import rx.Subscriber;

/**
 * Created by AndreHF on 10/3/2017.
 */

public class Observable implements rx.Observable.OnSubscribe<String>{
    private final Bitmap bitmap;
    private String dirName = "Exera-media";
    private File dir;


    public Observable(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void call(Subscriber<? super String> subscriber) {
        createDir();
        File noMedia = new File(dir.getPath(), ".nomedia");
        File fileImage = new File(dir.getPath(), "image-" + Calendar.getInstance().
                getTimeInMillis()+".jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            noMedia.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        subscriber.onNext(fileImage.getAbsolutePath());
    }

    private void createDir() {
        dir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!dir.exists())
            dir.mkdir();
    }


}
