package com.eshen.voucherunion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sin on 2020/5/4
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        bind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract int getLayoutResId();

    protected void initView() {

    }

    protected void initEvent() {

    }

    protected void initPresenter() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        this.release();
    }

    protected void release() {

    }
}
