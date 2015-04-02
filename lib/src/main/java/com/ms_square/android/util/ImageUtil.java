package com.ms_square.android.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class ImageUtil {

    public static Bitmap createNewBitmap(int width, int height){
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    // Not that the returned bitmap could be null or already recycled
    @Nullable
    public static Bitmap getBitmapFromDrawable(Drawable drawable){
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static Bitmap getBitmapFromResource(Resources resources, @DrawableRes int resId) {
        return BitmapFactory.decodeResource(resources, resId);
    }

    public static Bitmap drawTextOnBitmap(String text, int textColor, float textSize, Bitmap base) {
        return drawTextOnBitmap(text, textColor, textSize, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), base);
    }

    public static Bitmap drawTextOnBitmap(String text, int textColor, float textSize, Typeface typeFace, Bitmap base) {
        Bitmap synthesized = createNewBitmap(base.getWidth(), base.getHeight());
        Canvas canvas = new Canvas(synthesized);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        Rect textBounds = new Rect();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTypeface(typeFace);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, (base.getWidth() - textBounds.width()) /2,
                (base.getHeight() - textBounds.height()) /2 + textBounds.height(), paint);
        base.recycle();
        return synthesized;
    }

    public static Bitmap synthesize(Bitmap base, Bitmap overlay, boolean centerOverlay){
        Bitmap synthesized = createNewBitmap(base.getWidth(), base.getHeight());
        Canvas canvas = new Canvas(synthesized);
        canvas.drawBitmap(base, 0, 0, null);

        if (centerOverlay) {
            // draws the overlay on the center of the base
            canvas.drawBitmap(overlay, (base.getWidth() - overlay.getWidth()) /2,
                    (base.getHeight() - overlay.getHeight()) /2, null);
        } else {
            canvas.drawBitmap(overlay, 0, 0, null);
        }

        base.recycle();
        overlay.recycle();
        return synthesized;
    }

    public static Bitmap synthesize(Bitmap base, Bitmap overlay, PorterDuff.Mode mode){
        Bitmap synthesized = createNewBitmap(base.getWidth(), base.getHeight());
        Canvas canvas = new Canvas(synthesized);
        Paint paint = new Paint();
        canvas.drawBitmap(base, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(overlay, 0, 0, paint);
        base.recycle();
        overlay.recycle();
        return synthesized;
    }

    public static int getImageViewWidth(@NonNull ImageView imageView) {
        return imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
    }

    public static int getImageViewHeight(@NonNull ImageView imageView) {
        return imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
    }
}