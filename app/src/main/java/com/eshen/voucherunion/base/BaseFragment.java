package com.eshen.voucherunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sin on 2020/5/2
 */
public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View successView;
    private View loadingView;
    private View errorView;
    private View emptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private Unbinder bind;
    private FrameLayout baseContainer;

    @OnClick(R.id.network_error_tips)
    public void retry() {
        LogUtils.d(BaseFragment.this, "重试被点击");
        onRetryClick();
    }

    protected void onRetryClick() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LoadRootView(inflater, container);
        baseContainer = rootView.findViewById(R.id.base_container);
        loadStateView(inflater, container);
        bind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 加载各种状态的View
     *
     * @param inflater
     * @param container
     */
    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        //成功的View
        successView = loadSuccessView(inflater, container);
        baseContainer.addView(successView);
        //加载的View
        loadingView = LoadLoadingView(inflater, container);
        baseContainer.addView(loadingView);
        //错误的View
        errorView = LoadErrorView(inflater, container);
        baseContainer.addView(errorView);
        //空的View
        emptyView = LoadEmptyView(inflater, container);
        baseContainer.addView(emptyView);
        setUpState(State.NONE);
    }

    /**
     * 子类通过这个方法切换状态页面
     *
     * @param currentState
     */
    public void setUpState(State currentState) {
        this.currentState = currentState;
        successView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    protected void initView(View rootView) {

    }

    protected void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void loadData() {
        //加载数据
    }

    protected void initPresenter() {
        //创建Presenter
    }

    /**
     * 加载加载中界面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View LoadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    /**
     * 加载错误界面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View LoadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    /**
     * 加载空界面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View LoadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    protected View LoadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base_layout, container, false);
    }

    protected abstract int getRootViewResId();
}
