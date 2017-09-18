package com.javahelps.minesweeper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Button;


/**
 * Created by Sneha on 02-09-2017.
 */

public class MyButton extends Button {
    int value;
    int pos_x;
    int pos_y;
    boolean isFlag;

    public MyButton(Context context) {
        super(context);
        setPadding(0, 0, 0, 0);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.normal_button));
    }
}
