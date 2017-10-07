package com.app.exera.camera.custom.button;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

import com.app.exera.camera.utils.ReadFont;


public class CustomButtonBook extends Button {
    private ReadFont readFont;

    public CustomButtonBook(Context context) {
        super(context);
        setFont(context);
    }

    public CustomButtonBook(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);

    }

    public CustomButtonBook(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomButtonBook(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont(context);

    }


    protected void setFont(Context context) {
        readFont = new ReadFont(context);
        setTypeface(readFont.regular());
    }


}
