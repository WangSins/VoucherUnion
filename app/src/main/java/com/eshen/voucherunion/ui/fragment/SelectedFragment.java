package com.eshen.voucherunion.ui.fragment;

import android.view.View;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;

/**
 * Created by Sin on 2020/5/2
 */
public class SelectedFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

}
