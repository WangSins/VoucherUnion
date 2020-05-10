package com.eshen.voucherunion.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.model.domain.IBaseInfo;
import com.eshen.voucherunion.model.domain.OnSellContent;
import com.eshen.voucherunion.presenter.IOnSellPagePresenter;
import com.eshen.voucherunion.ui.adapter.OnSellPageContentAdapter;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.utils.SizeUtils;
import com.eshen.voucherunion.utils.TicketUtil;
import com.eshen.voucherunion.utils.ToastUtils;
import com.eshen.voucherunion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {

    public static final int DEFAULT_SPAN_COUNT = 2;
    private IOnSellPagePresenter onSellPagePresenter;

    @BindView(R.id.fragment_bar_title)
    public TextView barTitle;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView onSellContentList;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout onSellRefreshLayout;

    private OnSellPageContentAdapter onSellPageContentAdapter;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected View LoadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        barTitle.setText(R.string.text_on_sell_title);
        onSellContentList.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT));
        onSellPageContentAdapter = new OnSellPageContentAdapter();
        onSellContentList.setAdapter(onSellPageContentAdapter);
        onSellContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 4);
                outRect.bottom = SizeUtils.dip2px(getContext(), 4);
                outRect.left = SizeUtils.dip2px(getContext(), 4);
                outRect.right = SizeUtils.dip2px(getContext(), 4);
            }
        });
        onSellRefreshLayout.setEnableRefresh(false);
        onSellRefreshLayout.setEnableLoadmore(true);
        onSellRefreshLayout.setEnableOverScroll(false);
        onSellRefreshLayout.setBottomHeight(SizeUtils.dip2px(getContext(), 30));
    }

    @Override
    protected void initPresenter() {
        onSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        onSellPagePresenter.registerViewCallback(this);
        onSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void initListener() {
        onSellRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多
                if (onSellPagePresenter != null) {
                    onSellPagePresenter.LoaderMore();
                }
            }
        });
        onSellPageContentAdapter.setOnListItemClickListener(new OnSellPageContentAdapter.OnSellPageItemClickListener() {
            @Override
            public void onSellItemClick(IBaseInfo item) {
                handleItemClick(item);
            }
        });
    }

    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(),item);
    }

    @Override
    public void onContentLoaded(OnSellContent result) {
        setUpState(State.SUCCESS);
        onSellPageContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        onSellPageContentAdapter.addData(moreResult);
        if (onSellRefreshLayout != null) {
            onSellRefreshLayout.finishLoadmore();
        }
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了" + size + "条数据");
    }

    @Override
    public void onMoreLoadedError() {
        if (onSellRefreshLayout != null) {
            onSellRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        if (onSellRefreshLayout != null) {
            onSellRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("没有更多的商品");
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void onRetryClick() {
        if (onSellPagePresenter != null) {
            onSellPagePresenter.reLoaded();
        }
    }

    @Override
    protected void release() {
        if (onSellPagePresenter != null) {
            onSellPagePresenter.unregisterViewCallback(this);
        }
    }
}
