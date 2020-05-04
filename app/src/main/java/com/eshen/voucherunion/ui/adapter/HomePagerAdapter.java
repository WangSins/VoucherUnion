package com.eshen.voucherunion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sin on 2020/5/2
 */
public class HomePagerAdapter extends FragmentPagerAdapter {


    private List<Categories.DataBean> categories = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomePagerFragment homePagerFragment = HomePagerFragment.newInstance(categories.get(position));
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    public void setCategories(Categories categories) {
        this.categories.clear();
        List<Categories.DataBean> data = categories.getData();
        this.categories.addAll(data);
        notifyDataSetChanged();
    }
}
