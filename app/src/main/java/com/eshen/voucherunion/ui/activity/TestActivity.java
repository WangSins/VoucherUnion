package com.eshen.voucherunion.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sin on 2020/5/2
 */
public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        navigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.d(TestActivity.class, "checkedId-->" + checkedId);
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
