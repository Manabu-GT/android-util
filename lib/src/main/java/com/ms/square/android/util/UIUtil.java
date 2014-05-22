package com.ms.square.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;

public class UIUtil {

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
}