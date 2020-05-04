package com.eshen.voucherunion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.eshen.voucherunion.R;

/**
 * Created by Sin on 2020/5/4
 */
public class LoadingView extends AppCompatImageView {

    private float degrees = 0;
    private boolean needRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void startRotate() {
        needRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                degrees += 10;
                if (degrees >= 360) {
                    degrees = 0;
                }
                invalidate();
                //判断是否继续旋转
                //如果是不可见或者DetachedFromWindow就不旋转了
                if (getVisibility() != VISIBLE && !needRotate) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }
            }
        });
    }

    private void stopRotate() {
        needRotate = false;
    }
}
