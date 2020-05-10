package com.eshen.voucherunion.ui.activity;

import android.widget.RadioGroup;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigation;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEvent() {
        navigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.test_home:
                        break;
                    case R.id.test_selected:
                        break;
                    case R.id.test_red_packet:
                        break;
                    case R.id.test_search:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
