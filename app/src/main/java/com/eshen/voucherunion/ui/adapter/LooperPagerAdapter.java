package com.eshen.voucherunion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.eshen.voucherunion.model.domain.HomePagerContent;
import com.eshen.voucherunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sin on 2020/5/3
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> data = new ArrayList<>();
    private OnLooperPagerItemClickListener itemClickListener;

    public int getDataSize() {
        return data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //处理越界问题
        int realPosition = position % data.size();
        HomePagerContent.DataBean dataBean = data.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        int ivSize = (measuredWidth > measuredHeight ? measuredWidth : measuredHeight) / 2;

        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(), ivSize);
        Context context = container.getContext();
        ImageView iv = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(coverUrl).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onLooperItemClick(dataBean);
                }
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperPagerItemClickListener(OnLooperPagerItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnLooperPagerItemClickListener {
        void onLooperItemClick(HomePagerContent.DataBean item);
    }
}
