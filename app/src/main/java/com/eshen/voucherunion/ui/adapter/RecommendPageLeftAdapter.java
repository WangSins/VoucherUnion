package com.eshen.voucherunion.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sin on 2020/5/5
 */
public class RecommendPageLeftAdapter extends RecyclerView.Adapter<RecommendPageLeftAdapter.InnerHolder> {

    private List<RecommendPageCategory.DataBean> data = new ArrayList<>();
    private int currentSelectedPosition = 0;
    private OnLeftItemClickListener itemClickListener;

    @NonNull
    @Override
    public RecommendPageLeftAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendPageLeftAdapter.InnerHolder holder, int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (currentSelectedPosition == position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorBackground));
        } else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white));
        }
        RecommendPageCategory.DataBean dataBean = data.get(position);
        itemTv.setText(dataBean.getFavorites_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && currentSelectedPosition != position) {
                    //修改选中位置
                    currentSelectedPosition = position;
                    itemClickListener.onLeftItemClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(RecommendPageCategory categories) {
        List<RecommendPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
        if (this.data.size() > 0) {
            itemClickListener.onLeftItemClick(this.data.get(currentSelectedPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(RecommendPageCategory.DataBean item);
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
