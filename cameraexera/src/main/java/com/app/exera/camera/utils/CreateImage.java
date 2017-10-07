package com.app.exera.camera.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;


import com.app.exera.camera.callback.CallBackCreateImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


/**
 * Created by AndreHF on 9/29/2016.
 */

public class CreateImage extends AsyncTask<Void, Void, String> {
    private final Bitmap bitmap;
    private final CallBackCreateImage callBackCreateImage;
    private String dirName = "Exera-media";
    private File dir;
    private ProgressDialog progressDialog;
    private final Context context;

    public CreateImage(Bitmap bitmap, CallBackCreateImage callBackCreateImage, Context context) {
        this.bitmap = bitmap;
        this.callBackCreateImage = callBackCreateImage;
        this.context = context;
    }



    @Override
    protected String doInBackground(Void... params) {
        createDir();
        File noMedia = new File(dir.getPath(), ".nomedia");
        File fileImage = new File(dir.getPath(), "image-" + Calendar.getInstance().
                getTimeInMillis()+".jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            noMedia.createNewFile();
            return fileImage.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();
        callBackCreateImage.absolutPath(s);
    }

    private void createDir() {
        dir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!dir.exists())
            dir.mkdir();
    }
}
