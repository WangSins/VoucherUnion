package com.eshen.voucherunion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.eshen.voucherunion.model.domain.OnSellContent;
import com.eshen.voucherunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sin on 2020/5/10
 */
public class OnSellPageContentAdapter extends RecyclerView.Adapter<OnSellPageContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> data = new ArrayList<>();
    private OnSellPageItemClickListener itemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemBean = data.get(position);
        holder.setData(itemBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onSellItemClick(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(OnSellContent result) {
        this.data.clear();
        this.data.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    public void addData(OnSellContent result) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreResult = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //添加之前拿到原来的size
        int olderSize = data.size();
        data.addAll(moreResult);
        notifyItemRangeChanged(olderSize, moreResult.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_cover)
        public ImageView coverIv;

        @BindView(R.id.on_sell_title)
        public TextView titleTv;

        @BindView(R.id.on_sell_origin_prise)
        public TextView originalPriseTv;

        @BindView(R.id.on_sell_off_origin_prise)
        public TextView offOriginalPriseTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemBean) {
            Context context = itemView.getContext();
            String originalPrise = itemBean.getZk_final_price();
            long couponAmount = itemBean.getCoupon_amount();
            float resultPrise = Float.parseFloat(originalPrise) - couponAmount;

            String coverPath = UrlUtils.getCoverPath(itemBean.getPict_url());
            Glide.with(context).load(coverPath).into(coverIv);
            titleTv.setText(itemBean.getTitle());
            originalPriseTv.setText(String.format(context.getString(R.string.text_goods_original_prise), originalPrise));
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            offOriginalPriseTv.setText(String.format(context.getString(R.string.text_goods_after_off_prise), resultPrise));

        }
    }

    public void setOnListItemClickListener(OnSellPageItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnSellPageItemClickListener {
        void onSellItemClick(IBaseInfo item);
    }
}
