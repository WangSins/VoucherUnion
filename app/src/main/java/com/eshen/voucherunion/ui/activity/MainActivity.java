package com.eshen.voucherunion.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseActivity;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.ui.fragment.HomeFragment;
import com.eshen.voucherunion.ui.fragment.OnSellFragment;
import com.eshen.voucherunion.ui.fragment.RecommendFragment;
import com.eshen.voucherunion.ui.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private OnSellFragment onSellFragment;
    private RecommendFragment recommendFragment;
    private SearchFragment searchFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        homeFragment = new HomeFragment();
        onSellFragment = new OnSellFragment();
        recommendFragment = new RecommendFragment();
        searchFragment = new SearchFragment();
        switchFragment(homeFragment);
    }

    @Override
    protected void initEvent() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        switchFragment(homeFragment);
                        break;
                    case R.id.selected:
                        switchFragment(recommendFragment);
                        break;
                    case R.id.red_packet:
                        switchFragment(onSellFragment);
                        break;
                    case R.id.search:
                        switchFragment(searchFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //上一次显示的fragment
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //如果上一个Fragment和要切换的是同一个
        if (lastOneFragment == targetFragment) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_pager_container, targetFragment);
        } else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        transaction.commit();
    }

    @Override
    public void switch2Search() {
        //切换导航栏选中
        navigationView.setSelectedItemId(R.id.search);
    }
}
