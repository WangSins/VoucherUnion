package com.eshen.voucherunion.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.ui.fragment.HomeFragment;
import com.eshen.voucherunion.ui.fragment.RedPacketFragment;
import com.eshen.voucherunion.ui.fragment.SearchFragment;
import com.eshen.voucherunion.ui.fragment.SelectedFragment;
import com.eshen.voucherunion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private RedPacketFragment redPacketFragment;
    private SelectedFragment selectedFragment;
    private SearchFragment searchFragment;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    private void initListener() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                LogUtils.d(MainActivity.this, "title-->" + item.getTitle() + ",id-->" + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.home:
                        switchFragment(homeFragment);
                        break;
                    case R.id.selected:
                        switchFragment(selectedFragment);
                        break;
                    case R.id.red_packet:
                        switchFragment(redPacketFragment);
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

    private void initFragment() {
        homeFragment = new HomeFragment();
        redPacketFragment = new RedPacketFragment();
        selectedFragment = new SelectedFragment();
        searchFragment = new SearchFragment();
        switchFragment(homeFragment);
    }
}
