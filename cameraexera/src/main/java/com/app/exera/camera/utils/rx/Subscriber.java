package com.app.exera.camera.utils.rx;

import com.app.exera.camera.callback.CallBackCreateImage;

import lombok.Setter;

/**
 * Created by AndreHF on 10/3/2017.
 */

public class Subscriber extends rx.Subscriber<String> {

    private CallBackSubscriber callBackSubscriber;
    private CallBackCreateImage callBackCreateImage;

    public Subscriber setCallBackSubscriber(CallBackSubscriber callBackSubscriber) {
        this.callBackSubscriber = callBackSubscriber;
        return this;
    }
    public Subscriber setCallBackCreateImage(CallBackCreateImage callBackCreateImage) {
        this.callBackCreateImage = callBackCreateImage;
        return this;
    }

    @Override
    public void onCompleted() {
        unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        callBackSubscriber.onServiceFinish();
    }

    @Override
    public void onNext(String path) {
        callBackCreateImage.absolutPath(path);
        callBackSubscriber.onServiceFinish();
    }
}
