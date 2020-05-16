package com.eshen.voucherunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eshen.voucherunion.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sin on 2020/5/16
 */
public class TextFlowLayout extends ViewGroup {
    private List<String> textList = new ArrayList<>();
    public static final float DEFAULT_SPACE = 10;
    private float itemHorizontalSpace = DEFAULT_SPACE;
    private float itemVerticalSpace = DEFAULT_SPACE;
    private int selfWidth;
    private int itemHeight;
    private OnFlowTextItemClickListener itemClickListener;

    public float getItemHorizontalSpace() {
        return itemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        this.itemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return itemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        this.itemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //去拿到相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        itemHorizontalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_horizontalSpace, DEFAULT_SPACE);
        itemVerticalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_verticalSpace, DEFAULT_SPACE);
        typedArray.recycle();

    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.textList.clear();
        this.textList.addAll(textList);
        Collections.reverse(this.textList);
        //遍历内容
        for (String s : this.textList) {
            //添加View
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            textView.setText(s);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onFlowTextItemClick(s);
                    }
                }
            });
            addView(textView);
        }
    }

    public int getTextListSize() {
        return textList.size();
    }

    //所有行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        //单行
        List<View> line = null;
        lines.clear();
        selfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int childCount = getChildCount();
        //遍历获得孩子
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要进行测量
                continue;
            }
            //测量孩子
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            if (line == null) {
                //说明当前行空
                line = createNewLine(itemView);
            } else {
                //判断是否可以再添加
                if (canBeAdd(itemView, line)) {
                    //可以添加进去
                    line.add(itemView);
                } else {
                    //新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        //测量自己
        itemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * itemHeight + itemVerticalSpace * (lines.size() + 1) + 0.5f);
        setMeasuredDimension(selfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 当前行是否可以再继续添加
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有子View的宽度相加，加(line.size()+1)*itemHorizontalSpace+itemView.getMeasuredWidth()
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //叠加所有已经添加View的宽度
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWidth += itemHorizontalSpace * (line.size() + 1);
        //条件，如果小于当前控件的宽度，可以添加
        return totalWidth <= selfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        int topOffset = (int) itemVerticalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) itemHorizontalSpace;
            for (View view : views) {
                //每一行里每一个item
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + itemHorizontalSpace;
            }
            topOffset += itemHeight + itemVerticalSpace;
        }
    }

    public interface OnFlowTextItemClickListener {
        void onFlowTextItemClick(String s);
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
