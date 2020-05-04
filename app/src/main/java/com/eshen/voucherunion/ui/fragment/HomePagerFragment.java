package com.eshen.voucherunion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.model.domain.HomePagerContent;
import com.eshen.voucherunion.presenter.ICategoryPagerPresenter;
import com.eshen.voucherunion.presenter.impl.CategoryPagerPresenterImpl;
import com.eshen.voucherunion.ui.adapter.HomePagerContentAdapter;
import com.eshen.voucherunion.ui.adapter.LooperPagerAdapter;
import com.eshen.voucherunion.ui.custom.AutoLoopViewPager;
import com.eshen.voucherunion.utils.Constant;
import com.eshen.voucherunion.utils.LogUtils;
import com.eshen.voucherunion.utils.SizeUtils;
import com.eshen.voucherunion.utils.ToastUtils;
import com.eshen.voucherunion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.CustomNestedScrollView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter categoryPagerPresenter;
    private int materialId;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView contentList;

    private HomePagerContentAdapter contentAdapter;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    private LooperPagerAdapter looperPagerAdapter;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_nested_scroller)
    public CustomNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    public static HomePagerFragment newInstance(Categories.DataBean categories) {

        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_HOME_PAGER_MATERIAL_ID, categories.getId());
        bundle.putString(Constant.KEY_HOME_PAGER_TITLE, categories.getTitle());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        contentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);

            }
        });
        //创建适配器
        contentAdapter = new HomePagerContentAdapter();
        //设置适配器
        contentList.setAdapter(contentAdapter);

        //创建轮播图适配器
        looperPagerAdapter = new LooperPagerAdapter();
        //设置轮播图适配器
        looperPager.setAdapter(looperPagerAdapter);

        //设置相关内容
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
        twinklingRefreshLayout.setBottomHeight(SizeUtils.dip2px(getContext(), 30));
    }

    @Override
    protected void initListener() {
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //
                if (homePagerParent == null) {
                    return;
                }
                int measuredHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                contentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        homeHeaderContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                if (headerHeight != 0) {
                    homeHeaderContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (looperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % looperPagerAdapter.getDataSize();
                //切换指示器
                upDateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this, "触发加载更多...");
                if (categoryPagerPresenter != null) {
                    categoryPagerPresenter.LoaderMore(materialId);
                }

            }
        });
    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */
    private void upDateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_select);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initPresenter() {
        categoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        categoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            materialId = arguments.getInt(Constant.KEY_HOME_PAGER_MATERIAL_ID);
            String title = arguments.getString(Constant.KEY_HOME_PAGER_TITLE);
            //加载数据
            LogUtils.d(HomePagerFragment.this, "title-->" + title);
            LogUtils.d(HomePagerFragment.this, "materialId-->" + materialId);
            if (currentCategoryTitleTv != null) {
                currentCategoryTitleTv.setText(title);
            }
            if (categoryPagerPresenter != null) {
                categoryPagerPresenter.getContentByCategoryId(materialId);
            }
        }
    }

    @Override
    public int getCategoryId() {
        return materialId;
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载到了
        contentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的底部
        contentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLoaderMoreError() {
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onLoaderMoreEmpty() {
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("没有更多的商品");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(HomePagerFragment.this, "looper size -->" + contents.size());
        looperPagerAdapter.setData(contents);
        //中间点%数据的Size不一定为0，所以显示不是第一个
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        //设置中间点
        looperPager.setCurrentItem(targetCenterPosition);
        looperPointContainer.removeAllViews();
        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 10);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_select);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.unregisterViewCallback(this);
        }
    }
}
