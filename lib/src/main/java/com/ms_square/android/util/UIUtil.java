package com.ms_square.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

public class UIUtil {

    private static final String TAG = UIUtil.class.getSimpleName();

    public static float convertToPixelFromDip(Context ctx, float dip) {
        float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                ctx.getResources().getDisplayMetrics());
        return pixel;
    }

    public static float convertToPixelFromSp(Context ctx, float sp) {
        float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                ctx.getResources().getDisplayMetrics());
        return pixel;
    }

    public static Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Bitmap drawViewToBitmap(View view, int downSampling) {
        return drawViewToBitmap(view, 0f, 0f, downSampling);
    }

    public static Bitmap drawViewToBitmap(View view, float translateX, float translateY, int downSampling) {
        float scale = 1f / downSampling;
        int width = view.getWidth();
        int height = view.getHeight();
        int bmpWidth = (int) (width * scale - translateX/downSampling);
        int bmpHeight = (int) (height * scale - translateY/downSampling);
        Bitmap dest = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dest);
        c.translate(-translateX/downSampling, -translateY/downSampling);
        if (downSampling > 1) {
            c.scale(scale, scale);
        }
        view.draw(c);
        return dest;
    }

    // for debugging purpose
    public static void dumpViewTree(View v, String padding){
        Log.d(TAG, padding + v.getClass().getName());
        if(v instanceof ViewGroup){
            ViewGroup g = (ViewGroup) v;
            for(int i = 0; i < g.getChildCount(); i++){
                dumpViewTree(g.getChildAt(i), padding + " ");
            }
        }
    }
}