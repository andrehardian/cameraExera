# cameraExera
android camera library from laxus exera technology

## Preview
![1 0 exera camera](https://github.com/andrehardian/cameraExera/blob/master/art/1.0%20Exera%20Camera.png)
![2 0 exera camera - cut image](https://github.com/andrehardian/cameraExera/blob/master/art/2.0%20Exera%20Camera%20-%20Cut%20Image.png)

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
     compile 'com.github.andrehardian:cameraExera:1.0.5'
}

```
using NoActionBar theme
`
Theme.AppCompat.Light.NoActionBar
`

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
//        for hide gallery in camera
        cameraRequired.setHideGalery(true);

        startActivityForResult(new Intent(this, CameraExera.class).putExtra(CameraExera.BUNDLE_KEY,cameraRequired), REQUEST_CODE);

```

or

```
        startActivityForResult(new Intent(this, CameraExera.class).putExtra(CameraExera.BUNDLE_KEY,
                new CameraRequired().setWidth(400).setHeight(400).setHideGalery(true)), REQUEST_CODE);
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

## Credit
Almost all of the code for these library projects is based on:
1. [android image cropper](https://github.com/ArthurHub/Android-Image-Cropper)
2. [Project Lombok](https://projectlombok.org/)
3. [Rx Android](https://github.com/ReactiveX/RxAndroid)
4. [Rx Java](https://github.com/ReactiveX/RxJava)

## License
cameraExera by [andrehardian](https://github.com/andrehardian) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
