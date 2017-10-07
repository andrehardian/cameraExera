package com.app.exera.camera.utils;

import android.content.Context;
import android.graphics.Typeface;

public class ReadFont {
    private final Context context;
    String fontName;

    public ReadFont(Context context) {
        this.context = context;
    }

    public Typeface bold() {
        return font("GOTHAM-BOLD.ttf");
    }

    public Typeface boldItalic() {
        return font("GOTHAM-BOLDITALIC.ttf");
    }

    public Typeface italic() {
        return font("Lato-Italic.ttf");
    }

    public Typeface light() {
        return font("Lato-Light.ttf");
    }

    public Typeface lightItalic() {
        return font("Lato-LightItalic.ttf");
    }

    public Typeface regular() {
        return font("gotham-book.ttf");
    }

    public Typeface medium() {
        return font("GOTHAM-MEDIUM.ttf");
    }

    public Typeface mediumItalic() {
        return font("gotham-LightItalic.ttf");
    }


    private Typeface font(String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
        return font;
    }
}
