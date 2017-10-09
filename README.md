# cameraExera
android camera library from laxus exera technology

## Usage

### Include Library
Add it in your root build.gradle at the end of repositories:

```
allprojects {
   repositories {
    maven { url "https://jitpack.io" }
   }
}
```
Add dependencies :

```
dependencies {
     compile 'com.github.andrehardian:cameraExera:1.0.2'
}

```
### Using activity
Start CameraExera using builder pattern from your activity

1. start activity result

`startActivityForResult(new Intent(this, CameraExera.class), REQUEST_CODE);
`

2. add data for managing camera and cropping
```
        CameraRequired cameraRequired = new CameraRequired();
//        set width crop image
        cameraRequired.setWidth(400);
//        set height crop image
        cameraRequired.setHeight(400);
//        set array index image, if not in array dont fill this
        cameraRequired.setArrayIndex(0);
//        for disable gallery(old version for hide gallery in camera)
        cameraRequired.setHideGalery(true);

        startActivityForResult(new Intent(this, CameraExera.class).putExtra(CameraExera.BUNDLE_KEY,cameraRequired), REQUEST_CODE);

```

3. get data from `CameraExera` 

```
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
//                get path image
                String path = data.getStringExtra(UtilsCamera.resultTagCamera);
//                get position image in array
                int positionArray = data.getIntExtra(CameraExera.positionImage, 0);

//                get bitmap using path
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if (path != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                }
            }
        }
    }
```

## License
cameraExera by `andrehardian` is licensed under a `Apache License 2.0.`
