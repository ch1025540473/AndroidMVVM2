package com.mx.framework.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.mx.engine.utils.CheckUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuxuan on 16/6/3.
 */
@Deprecated
public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    List<BaseFragment> fragmentList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addBaseFragment(@NonNull BaseFragment baseFragment) {
        CheckUtils.checkNotNull(baseFragment);
        fragmentList.add(baseFragment);
    }

    public void addBaseFragments(@NonNull List<BaseFragment> baseFragments) {
        CheckUtils.checkNotNull(baseFragments);
        fragmentList.addAll(baseFragments);

    }
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}
