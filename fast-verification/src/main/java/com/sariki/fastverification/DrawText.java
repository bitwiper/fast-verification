package com.sariki.fastverification;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author sariki
 * @date 2020/04/02.
 */
@SuppressLint("AppCompatCustomView")
public class DrawText extends TextView {

    public DrawText(Context context) {
        super(context);
        init();
    }

    public DrawText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        this.setGravity(Gravity.CENTER);
    }

    public void darwText(String str) {
        this.setText(str);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }
}
