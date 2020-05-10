package com.eshen.voucherunion.ui.adapter;

import android.content.Context;
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
import com.eshen.voucherunion.model.domain.RecommendContent;
import com.eshen.voucherunion.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sin on 2020/5/5
 */
public class RecommendPageContentAdapter extends RecyclerView.Adapter<RecommendPageContentAdapter.InnerHolder> {

    private List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> data = new ArrayList<>();
    private OnSelectedPageContentItemClickListener itemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemBean = this.data.get(position);
        holder.setData(itemBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onContentItemClick(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(RecommendContent content) {
        if (content.getCode() == Constant.SUCCESS_CODE) {
            List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> data = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recommend_cover)
        public ImageView coverIv;

        @BindView(R.id.recommend_off_prise)
        public TextView offPriseTv;

        @BindView(R.id.recommend_title)
        public TextView titleTv;

        @BindView(R.id.recommend_buy_btn)
        public TextView buyTv;

        @BindView(R.id.recommend_original_prise)
        public TextView originalPriseTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemBean) {
            Context context = itemView.getContext();

            Glide.with(context).load(itemBean.getPict_url()).into(coverIv);
            if (TextUtils.isEmpty(itemBean.getCoupon_info())) {
                offPriseTv.setVisibility(View.GONE);
            } else {
                offPriseTv.setVisibility(View.VISIBLE);
                offPriseTv.setText(itemBean.getCoupon_info());
            }
            titleTv.setText(itemBean.getTitle());
            if (TextUtils.isEmpty(itemBean.getCoupon_click_url())) {
                originalPriseTv.setText("来晚了，没有优惠券了");
                buyTv.setVisibility(View.GONE);
            } else {
                buyTv.setVisibility(View.VISIBLE);
                originalPriseTv.setText(String.format(context.getString(R.string.text_recommend_original_prise), itemBean.getZk_final_price()));
            }
        }
    }

    public interface OnSelectedPageContentItemClickListener {
        void onContentItemClick(IBaseInfo item);
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
