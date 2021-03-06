package com.eshen.voucherunion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.presenter.IHomePresenter;
import com.eshen.voucherunion.ui.activity.IMainActivity;
import com.eshen.voucherunion.ui.activity.ScanQrCodeActivity;
import com.eshen.voucherunion.ui.adapter.HomePageAdapter;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter homePresenter;

    @BindView(R.id.home_indicator)
    public TabLayout tabLayout;
    @BindView(R.id.home_pager)
    public ViewPager viewPager;
    private HomePageAdapter homePageAdapter;

    @BindView(R.id.home_search_input_box)
    public EditText searchInputBox;
    @BindView(R.id.scan_icon)
    public ImageView scanBtn;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected View LoadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base_home_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        tabLayout.setupWithViewPager(viewPager);
        //给ViewPager设置适配器
        homePageAdapter = new HomePageAdapter(getChildFragmentManager());
        //设置适配器
        viewPager.setAdapter(homePageAdapter);
    }

    @Override
    protected void initPresenter() {
        //创建presenter
        homePresenter = PresenterManager.getInstance().getHomePresenter();
        homePresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        searchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2Search();
                }
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫码界面
                startActivity(new Intent(getContext(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    protected void loadData() {
        //加载数据
        homePresenter.getCategories();

    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        //加载的数据
        if (homePageAdapter != null) {
            homePageAdapter.setCategories(categories);
        }

    }

    @Override
    protected void onRetryClick() {
        //网络错误点击重试
        //重新加载分类
        if (homePresenter != null) {
            homePresenter.getCategories();
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
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        //取消注册
        if (homePresenter != null) {
            homePresenter.unregisterViewCallback(this);
        }
    }
}
