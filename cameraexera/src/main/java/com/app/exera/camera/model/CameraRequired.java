package com.app.exera.camera.model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by AndreHF on 10/3/2017.
 */

@Data
public class CameraRequired implements Serializable{
    public CameraRequired setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
        return this;
    }

    public CameraRequired setHideGalery(boolean hideGalery) {
        this.hideGalery = hideGalery;
        return this;
    }

    public CameraRequired setWidth(int width) {
        this.width = width;
        return this;
    }

    public CameraRequired setHeight(int height) {
        this.height = height;
        return this;
    }

    private int arrayIndex;
    private boolean hideGalery;
    private int width;
    private int height;

}
