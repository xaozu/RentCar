package com.xz.rentcar.util;

import android.app.Activity;
import android.graphics.Color;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.xz.rentcar.activity.BaseActivity;


/**
 * Author: lgp
 * Date: 2014/12/31.
 * 提示
 */
public class SnackbarUtils {

    public static void show(Activity activity, int message) {
        int color = Color.BLACK;
        if (activity instanceof BaseActivity){
            color = (((BaseActivity) activity)).getColorPrimary();
        }
        color = color & 0xddffffff;
        SnackbarManager.show(
                Snackbar.with(activity.getApplicationContext())
                        .color(color)
                        .duration((Snackbar.SnackbarDuration.LENGTH_SHORT.getDuration() / 2))
                        .text(message), activity);
    }

}
