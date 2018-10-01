package com.picolab.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.picolab.view.showImage;

public class ShowMessage {

    public void toast(Context context, String text){
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
