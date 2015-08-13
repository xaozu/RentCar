package com.xz.rentcar.util;

import android.app.Activity;

import com.xz.rentcar.R;


/**
 * 主题管理类
 */
public class ThemeUtils {
    /**
     * 改变主题
     * @param activity
     * @param theme
     */
    public static void changTheme(Activity activity, Theme theme){
        if (activity == null)
            return;
        int style = R.style.RedTheme;
        switch (theme){
            case BROWN:
                style = R.style.BrownTheme;
                break;
            case BLUE:
                style = R.style.BlueTheme;
                break;
            case BLUE_GREY:
                style = R.style.BlueGreyTheme;
                break;
            case YELLOW:
                style = R.style.YellowTheme;
                break;
            case DEEP_PURPLE:
                style = R.style.DeepPurpleTheme;
                break;
            case PINK:
                style = R.style.PinkTheme;
                break;
            case GREEN:
                style = R.style.GreenTheme;
                break;
            case GREY:
                style = R.style.GreyTheme;
                break;
            default:
                break;
        }
        activity.setTheme(style);
    }

    /**
     * 主题颜色枚举
     */
    public enum Theme{
        RED(0x00),
        BROWN(0x01),
        BLUE(0x02),
        BLUE_GREY(0x03),
        YELLOW(0x04),
        DEEP_PURPLE(0x05),
        PINK(0x06),
        GREY(0x08),
        GREEN(0x07);

        private int mValue;

        Theme(int value){
            this.mValue = value;
        }

        public static Theme mapValueToTheme(final int value) {
            for (Theme theme : Theme.values()) {
                if (value == theme.getIntValue()) {
                    return theme;
                }
            }
            // If run here, return default
            return getDefault();
        }

        static Theme getDefault()
        {
            return GREY;
        }
        public int getIntValue() {
            return mValue;
        }
    }
}
