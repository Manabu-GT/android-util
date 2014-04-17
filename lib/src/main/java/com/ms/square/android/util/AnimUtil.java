package com.ms.square.android.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;

public class AnimUtil {

    public static void setOnTouchScaleAnimation(View targetView, final float scaleX, final float scaleY) {
        targetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        ScaleAnimation anim = new ScaleAnimation(1.0f, scaleX, 1.0f,
                                scaleY, v.getWidth() / 2, v.getHeight() / 2);
                        anim.setDuration(60);
                        anim.setFillEnabled(true);
                        anim.setFillAfter(true);
                        v.startAnimation(anim);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        ScaleAnimation anim = new ScaleAnimation(scaleX, 1.0f, scaleY,
                                1.0f, v.getWidth() / 2, v.getHeight() / 2);
                        anim.setDuration(100);
                        v.startAnimation(anim);
                        break;
                    }
                }
                return false;
            }
        });
    }
}