package com.app.exera.camera.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.app.exera.camera.R;
import com.app.exera.camera.callback.CallBackCameraUtils;
import com.app.exera.camera.callback.CallBackCreateImage;
import com.app.exera.camera.model.CameraRequired;
import com.app.exera.camera.utils.PermissionMarshmellow;
import com.app.exera.camera.utils.PickImageChooserIntent;
import com.app.exera.camera.utils.UtilsCamera;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;


public class CameraExera extends AppCompatActivity implements CallBackCameraUtils,
        CallBackCreateImage, Camera.AutoFocusCallback {
    private FrameLayout cameraPreview;
    private CropImageView showImage;
    private FrameLayout cameraView;
    private FrameLayout imageViewing;
    private Button galery;
    private Button capture;
    private Button exit;
    private Button close;
    private ToggleButton flash;
    private Button crop;
    private Button swipe;
    private ImageView icon;


    private PermissionMarshmellow permissionMarshmellow;
    public final static String BUNDLE_KEY = "CAMERA_REQUIRED";

    private UtilsCamera utilsCamera;
    private CameraRequired cameraRequired;
    private static final int ACTION_LOAD_IMAGE = 100;
    private static int SIZE_W = 800;
    private static int SIZE_H = 600;
    private static final int FOCUS_AREA_SIZE = 300;

    private Bitmap cropped;

    private Camera camera;
    private CameraPreview cameraPreviewer;
    private PickImageChooserIntent pickImageChooserIntent;
    public static String positionImage = "position";
    private boolean flashOn = true;
    private Camera.Size size;
    private boolean cameraClick;
    private static boolean cameraFront;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContent();
        checkBundle();
        afterView();
    }

    private void initializeContent() {
        setContentView(R.layout.camera);
        cameraPreview = findView(R.id.camera_preview);
        showImage = findView(R.id.image_show);
        cameraView = findView(R.id.camera_view);
        imageViewing = findView(R.id.image_view);
        galery = findView(R.id.galery);
        capture = findView(R.id.capture);
        exit = findView(R.id.exit);
        close = findView(R.id.close);
        crop = findView(R.id.crop);
        swipe = findView(R.id.swipe);
        flash = findView(R.id.flash);
        icon = findView(R.id.icon);

        cameraPreview.setOnTouchListener((View view, MotionEvent motionEvent) ->
                cameraPreview(motionEvent));
        galery.setOnClickListener((View view) -> galery());
        capture.setOnClickListener((View view) -> capture());
        exit.setOnClickListener((View view) -> exit());
        close.setOnClickListener((View view) -> close());
        flash.setOnCheckedChangeListener((CompoundButton compoundButton, boolean check) -> flash
                (compoundButton, check));
        crop.setOnClickListener((View view) -> crop());
        swipe.setOnClickListener((View view) -> swipe());

        permissionMarshmellow = new PermissionMarshmellow(this);
        utilsCamera = new UtilsCamera(this);

        cameraRequired = new CameraRequired();
    }

    private void swipe() {
        cameraFront = cameraFront ? false : true;
        close();
    }

    protected <K extends View> K findView(int id) {
        return (K) findViewById(id);
    }


    private void checkBundle() {
        cameraRequired = (CameraRequired) getIntent().getSerializableExtra(BUNDLE_KEY);
        if (cameraRequired != null) {
            SIZE_W = cameraRequired.getWidth() != 0 ? cameraRequired.getWidth() : SIZE_W;
            SIZE_H = cameraRequired.getHeight() != 0 ? cameraRequired.getHeight() : SIZE_H;
        } else {
            cameraRequired = new CameraRequired();
        }
    }

    protected void afterView() {
        showImage.setFixedAspectRatio(true);
        showImage.setAspectRatio(SIZE_W, SIZE_H);
        showImage.setGuidelines(CropImageView.Guidelines.ON);
        showImage.setCropShape(CropImageView.CropShape.RECTANGLE);
        showImage.setScaleType(CropImageView.ScaleType.CENTER_CROP);
        setParamCrop();

        galery.setVisibility(cameraRequired.isHideGalery() ? View.GONE : View.VISIBLE);

        pickImageChooserIntent = new PickImageChooserIntent(this);
        utilsCamera.initCallBack(this);
        permissionCamera();
    }

    private void setParamCrop() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size.x,size.y);
        showImage.setLayoutParams(layoutParams);
    }

    private void permissionCamera() {
        if (!permissionMarshmellow.checkPermissionForCamera()) {
            permissionMarshmellow.requestPermissionForCamera();
        } else {
            openCamera();
        }

    }

    private void openCamera() {
        camera = utilsCamera.getCameraInstance(cameraFront ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK);
        setFlash();
        if (camera != null) {
            camera.setDisplayOrientation(90);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            float ratio = ((float)metrics.heightPixels / (float)metrics.widthPixels);
            cameraPreviewer = new CameraPreview(this, camera, this).
                    setDisplay(getWindowManager().getDefaultDisplay());
            cameraPreview.addView(cameraPreviewer);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PermissionMarshmellow.CAMERA_PERMISSION_REQUEST_CODE)
                openCamera();
            else if (requestCode == PermissionMarshmellow.WRITE_PERMISSION_REQUEST_CODE)
                createImage();
        }
    }

    private void setFlash() {
        try {
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(flashOn ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected void capture() {
        if (camera.getParameters().getFocusMode().equals(Camera.Parameters.FLASH_MODE_AUTO)) {
            cameraClick = true;
            camera.autoFocus(this);
        } else
            takePicture();

    }

    private void takePicture() {
        camera.takePicture(null, null, utilsCamera.picture(cameraFront));
    }


    protected void close() {
        camera.release();
        recreate();
    }


    protected void exit() {
        camera.release();
        finish();
    }


    protected void galery() {
        startActivityForResult(pickImageChooserIntent.resultPickImageChooser(), ACTION_LOAD_IMAGE);
    }

    protected void flash(CompoundButton compoundButton, boolean check) {
        flashOn = check;
        setFlash();
    }


    protected boolean cameraPreview(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            focusOnTouch(event);
        return true;
    }


    protected void crop() {
        final Bitmap bitmap = showImage.getCroppedImage();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                cropped = Bitmap.createScaledBitmap(bitmap, SIZE_W, SIZE_H, true);
                if (!permissionMarshmellow.checkPermissionForWrite())
                    permissionMarshmellow.requestPermissionForWrite();
                else
                    createImage();

            }
        });
    }

    private void createImage() {
        new com.app.exera.camera.utils.rx.CreateImage(this, this).subscribe(cropped);
    }

    private void result(String path) {
        Intent intent = new Intent();
        intent.putExtra(UtilsCamera.resultTagCamera, path);
        intent.putExtra(positionImage, cameraRequired.getArrayIndex());
        try {
            setResult(RESULT_OK, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        camera.release();
        onBackPressed();
    }

    @Override
    public void image(Bitmap bitmap) {
        imageViewing.setVisibility(View.VISIBLE);
        cameraView.setVisibility(View.GONE);
        showImage.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = pickImageChooserIntent.getPickImageResultUri(data);
            imageViewing.setVisibility(View.VISIBLE);
            cameraView.setVisibility(View.GONE);
            showImage.setImageUriAsync(imageUri);
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    @Override
    public void absolutPath(String path) {
        result(path);
    }

    @Override
    public void onBackPressed() {
        if (cameraPreview.getVisibility() == View.GONE)
            close();
        else
            exit();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success && cameraClick) {
            takePicture();
            cameraClick = false;
        }

    }

    private void focusOnTouch(MotionEvent event) {
        if (camera != null) {

            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                if (parameters.getSupportedFocusModes() != null && parameters.getSupportedFocusModes().
                        contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                camera.setParameters(parameters);
                camera.autoFocus(this);
            } else {
                camera.autoFocus(this);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / cameraPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / cameraPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }


}
