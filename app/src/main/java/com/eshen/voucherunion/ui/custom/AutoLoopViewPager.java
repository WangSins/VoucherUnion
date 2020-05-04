package com.eshen.voucherunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.eshen.voucherunion.R;

/**
 * Created by Sin on 2020/5/4
 */
public class AutoLoopViewPager extends ViewPager {

    public static final long DEFAULT_DURATION = 3000;

    private long duration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        super(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context, attrs);
    }

    private boolean isLoop = false;

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, 3000);
            }
        }
    };

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        //获取属性
        duration = typedArray.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        //回收
        typedArray.recycle();
    }

    /**
     * 设置切换时长
     *
     * @param duration 时长，单位：毫秒
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void startLoop() {
        isLoop = true;
        //先拿到当前位置
        post(task);
    }

    public void stopLoop() {
        isLoop = false;
        removeCallbacks(task);
    }
}
