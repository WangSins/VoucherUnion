package com.eshen.voucherunion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eshen.voucherunion.R;
import com.eshen.voucherunion.model.domain.IBaseInfo;
import com.eshen.voucherunion.model.domain.ILinearItemInfo;
import com.eshen.voucherunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sin on 2020/5/3
 */
public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    List<ILinearItemInfo> data = new ArrayList<>();
    private OnListItemClickListener itemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ILinearItemInfo dataBean = data.get(position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加之前拿到原来的size
        int olderSize = data.size();
        data.addAll(contents);
        notifyItemRangeChanged(olderSize, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView coverIv;
        @BindView(R.id.goods_title)
        public TextView titleTv;
        @BindView(R.id.goods_off_prise)
        public TextView offPriseTv;
        @BindView(R.id.goods_after_off_prise)
        public TextView finalPriseTv;
        @BindView(R.id.goods_original_prise)
        public TextView originalPriseTv;
        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            String finalPrice = dataBean.getFinalPrise();
            long couponAmount = dataBean.getCouponAmount();
            float resultPrise = Float.parseFloat(finalPrice) - couponAmount;

            ViewGroup.LayoutParams layoutParams = coverIv.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = (width > height ? width : height) / 2;
            String cover = dataBean.getCover();
            if (!TextUtils.isEmpty(cover)) {
                String coverPath = UrlUtils.getCoverPath(dataBean.getCover(),coverSize);
                Glide.with(context).load(coverPath).into(coverIv);
            }else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }

            titleTv.setText(dataBean.getTitle());
            offPriseTv.setText(String.format(context.getString(R.string.text_goods_off_prise), couponAmount));
            finalPriseTv.setText(String.format(context.getString(R.string.text_goods_after_off_prise), resultPrise));
            originalPriseTv.setText(String.format(context.getString(R.string.text_goods_original_prise), finalPrice));
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(IBaseInfo item);
    }
}
