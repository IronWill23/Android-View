package com.somoplay.eadate.View.FreeToast;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lm201 on 2016/9/3.
 */

public class FreeToast {

    private static float toastTextSize; //Toast Text Size
    private static int toastTextColor;  //Toast Text Color
    private static int toastBg;         //Toast Shape Style
    private static float toastHeight;   //Toast Height
    private static int toastGravity;    //Toast Location

    public FreeToast(float toastHeight, int toastTextColor, int toastBg, float toastTextSize, int toastGravity) {
        this.toastHeight = toastHeight;
        this.toastTextColor = toastTextColor;
        this.toastBg = toastBg;
        this.toastTextSize = toastTextSize;
        this.toastGravity = toastGravity;
    }

    /**
     * Toast字体大小
     */
    private static final float DEFAULT_TEXT_SIZE = 14;
    /**
     * Toast字体颜色
     */
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    /**
     * Toast背景颜色
     */
    private static final int DEFAULT_BG_COLOR = 0xE6f5695a;
    /**
     * Toast的高度(单位dp)
     */
    private static final float DEFAULT_TOAST_HEIGHT = 40.0f;

    private static Context mContext;
    private volatile static FreeToast mInstance;
    private static Toast mToast;
    private View layout;
    private TextView tv;

    public FreeToast(Context context) {
        mContext = context;
    }

    /**
     * Singleton
     */
    private static FreeToast getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FreeToast.class) {
                if (mInstance == null) {
                    mInstance = new FreeToast(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private static void getToast(int duration) {
        if (mToast == null) {
            mToast = new Toast(mContext);
            mToast.setGravity(toastGravity, 0, 0);
            //Setup Duration
            mToast.setDuration(duration == Toast.LENGTH_LONG ? Toast.LENGTH_LONG
                    : Toast.LENGTH_SHORT);
        }
    }

    public static void makeText(Context context, String text) {
        makeText(context, text, Toast.LENGTH_SHORT);
    }

    public static void makeText(Context context, String text, int duration) {
        getInstance(context);
        getToast(duration);
        if (mInstance.layout == null || mInstance.tv == null) {
            LinearLayout container = new LinearLayout(mContext);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            container.setLayoutParams(rootParams);
            container.setBackgroundResource(toastBg);
            container.setGravity(Gravity.CENTER);

            mInstance.tv = new TextView(mContext);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    getScreenWidth(mContext), dp2px(toastHeight));
            mInstance.tv.setLayoutParams(tvParams);
            mInstance.tv.setPadding(dp2px(5), dp2px(2), dp2px(5), dp2px(2));
            mInstance.tv.setGravity(Gravity.CENTER);
            mInstance.tv.setTextColor(toastTextColor);
            mInstance.tv.setMaxLines(2);
            mInstance.tv.setEllipsize(TextUtils.TruncateAt.END);
//            mInstance.tv.setBackgroundColor(toastBgColor);
            mInstance.tv.setTextSize(toastTextSize);

            container.addView(mInstance.tv);

            mInstance.layout = container;

            mToast.setView(mInstance.layout);
        }
        mInstance.tv.setText(text);
        mToast.show();
    }

    /**
     * transfer dp to px
     *
     * @param value dp
     * @return px
     */
    public static int dp2px(float value) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    /**
     * Window Width
     *
     * @param context Context
     * @return px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density;
        return (int) (outMetrics.widthPixels * density);
    }

}
