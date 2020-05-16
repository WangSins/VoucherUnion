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
import com.eshen.voucherunion.model.domain.HomePageContent;
import com.eshen.voucherunion.model.domain.IBaseInfo;
import com.eshen.voucherunion.presenter.ICategoryPagePresenter;
import com.eshen.voucherunion.ui.adapter.LinearItemContentAdapter;
import com.eshen.voucherunion.ui.adapter.LooperPageAdapter;
import com.eshen.voucherunion.ui.custom.AutoLoopViewPager;
import com.eshen.voucherunion.utils.Constant;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.utils.SizeUtils;
import com.eshen.voucherunion.utils.TicketUtil;
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
public class HomePageFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagePresenter categoryPagerPresenter;
    private int materialId;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView contentList;

    private LinearItemContentAdapter contentAdapter;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    private LooperPageAdapter looperPageAdapter;

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

    public static HomePageFragment newInstance(Categories.DataBean categories) {

        HomePageFragment homePageFragment = new HomePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_HOME_PAGER_MATERIAL_ID, categories.getId());
        bundle.putString(Constant.KEY_HOME_PAGER_TITLE, categories.getTitle());
        homePageFragment.setArguments(bundle);
        return homePageFragment;
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
        contentAdapter = new LinearItemContentAdapter();
        //设置适配器
        contentList.setAdapter(contentAdapter);

        //创建轮播图适配器
        looperPageAdapter = new LooperPageAdapter();
        //设置轮播图适配器
        looperPager.setAdapter(looperPageAdapter);

        //设置相关内容
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
        twinklingRefreshLayout.setEnableOverScroll(false);
        twinklingRefreshLayout.setBottomHeight(SizeUtils.dip2px(getContext(), 30));
    }

    @Override
    protected void initListener() {

        contentAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                handleItemClick(item);
            }
        });

        looperPageAdapter.setOnLooperPagerItemClickListener(new LooperPageAdapter.OnLooperPagerItemClickListener() {
            @Override
            public void onLooperItemClick(IBaseInfo item) {
                handleItemClick(item);
            }
        });

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
                if (looperPageAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % looperPageAdapter.getDataSize();
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
                if (categoryPagerPresenter != null) {
                    categoryPagerPresenter.LoaderMore(materialId);
                }

            }
        });
    }

    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(),item);
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
        categoryPagerPresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        categoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            materialId = arguments.getInt(Constant.KEY_HOME_PAGER_MATERIAL_ID);
            String title = arguments.getString(Constant.KEY_HOME_PAGER_TITLE);
            //加载数据
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
    public void onContentLoaded(List<HomePageContent.DataBean> contents) {
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
    public void onLoaderMoreLoaded(List<HomePageContent.DataBean> contents) {
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
    public void onLooperListLoaded(List<HomePageContent.DataBean> contents) {
        looperPageAdapter.setData(contents);
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
