package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sin on 2020/5/3
 */
public class CustomNestedScrollView extends NestedScrollView {
    private int headerHeight = 0;
    private int originScroll = 0;
    private RecyclerView recyclerView;

    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof RecyclerView) {
            this.recyclerView = (RecyclerView) target;
        }
        if (originScroll < headerHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    /**
     * 判断子类是否已经滑动到底部
     *
     * @return
     */
    public boolean isInBottom() {
        if (recyclerView != null) {
            boolean isBottom = !recyclerView.canScrollVertically(1);
            return isBottom;
        }
        return false;
    }
}
