package com.app.exera.camera.utils.rx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;

import com.app.exera.camera.callback.CallBackCreateImage;

import rx.*;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AndreHF on 10/3/2017.
 */

public class CreateImage implements CallBackSubscriber {
    private final Activity activity;
    private ProgressDialog progressDialog;
    private final CallBackCreateImage callBackCreateImage;

    public CreateImage(Activity activity, CallBackCreateImage callBackCreateImage) {
        this.activity = activity;
        this.callBackCreateImage = callBackCreateImage;
    }

    public void subscribe(Bitmap bitmap) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        rx.Observable.create(new Observable(bitmap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber().setCallBackCreateImage(callBackCreateImage)
                        .setCallBackSubscriber(this));
    }

    @Override
    public void onServiceFinish() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
