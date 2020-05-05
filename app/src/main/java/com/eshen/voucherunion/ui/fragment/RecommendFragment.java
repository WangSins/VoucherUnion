package com.eshen.voucherunion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.model.domain.RecommendContent;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;
import com.eshen.voucherunion.presenter.IRecommendPagePresenter;
import com.eshen.voucherunion.presenter.ITicketPresenter;
import com.eshen.voucherunion.ui.activity.TicketActivity;
import com.eshen.voucherunion.ui.adapter.RecommendPageContentAdapter;
import com.eshen.voucherunion.ui.adapter.RecommendPageLeftAdapter;
import com.eshen.voucherunion.utils.LogUtils;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.utils.SizeUtils;
import com.eshen.voucherunion.view.IRecommendPageCallback;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class RecommendFragment extends BaseFragment implements IRecommendPageCallback {

    private IRecommendPagePresenter recommendPagePresenter;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    private RecommendPageLeftAdapter leftAdapter;
    private RecommendPageContentAdapter contentAdapter;

    @BindView(R.id.right_content_loading)
    public View rightContentLoading;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        leftAdapter = new RecommendPageLeftAdapter();
        leftCategoryList.setAdapter(leftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        contentAdapter = new RecommendPageContentAdapter();
        rightContentList.setAdapter(contentAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
                outRect.left = SizeUtils.dip2px(getContext(), 4);
                outRect.right = SizeUtils.dip2px(getContext(), 4);
            }
        });
    }

    @Override
    protected void initListener() {
        leftAdapter.setOnLeftItemClickListener(new RecommendPageLeftAdapter.OnLeftItemClickListener() {
            @Override
            public void onLeftItemClick(RecommendPageCategory.DataBean item) {
                //左边分类点击了
                LogUtils.d(RecommendFragment.this, "onLeftItemClick-->" + item.getFavorites_title());
                recommendPagePresenter.getContentByCategoryId(item);
                rightContentList.setVisibility(View.GONE);
                rightContentLoading.setVisibility(View.VISIBLE);
            }
        });
        contentAdapter.setOnSelectedPageContentItemClickListener(new RecommendPageContentAdapter.OnSelectedPageContentItemClickListener() {
            @Override
            public void onContentItemClick(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
                //右侧内容被点击了
                String url = item.getCoupon_click_url();
                if (TextUtils.isEmpty(url)) {
                    url = item.getClick_url();
                }
                String title = item.getTitle();
                String cover = item.getPict_url();
                //拿到TickerPresenter去加载数据
                ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
                ticketPresenter.getTicket(url, title, cover);
                startActivity(new Intent(getContext(), TicketActivity.class));
            }
        });
    }

    @Override
    protected void initPresenter() {
        recommendPagePresenter = PresenterManager.getInstance().getRecommendPagePresenter();
        recommendPagePresenter.registerViewCallback(this);
        recommendPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        if (recommendPagePresenter != null) {
            recommendPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onCategoriesLoaded(RecommendPageCategory categories) {
        setUpState(State.SUCCESS);
        leftAdapter.setData(categories);
        //分类内容
        LogUtils.d(RecommendFragment.this, "onCategoriesLoaded -->" + categories.toString());
    }

    @Override
    public void onContentLoaded(RecommendContent content) {
        //分类下内容
        LogUtils.d(RecommendFragment.this, "onContentLoaded -->" + content.toString());
        contentAdapter.setData(content);
        rightContentList.scrollToPosition(0);
        rightContentList.setVisibility(View.VISIBLE);
        rightContentLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onRetryClick() {
        if (recommendPagePresenter != null) {
            recommendPagePresenter.reloadCategories();
        }
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

    }

}
