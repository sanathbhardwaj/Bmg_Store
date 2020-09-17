package com.digital.dhanbadbasket.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.digital.dhanbadbasket.fragments.ProductListFragment;

import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    List<String> titles;
    String cat, sub_cat, pos;

    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior, List<String> titles_list, String category, String sub_category, String position) {
        super(fm, behavior);
        titles = titles_list;
        cat = category;
        sub_cat = sub_category;
        pos = position;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProductListFragment.newInstance(position, getPageTitle(position).toString(), cat, sub_cat, pos);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }
}
