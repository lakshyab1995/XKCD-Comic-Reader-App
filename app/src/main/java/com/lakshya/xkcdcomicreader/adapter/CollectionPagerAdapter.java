package com.lakshya.xkcdcomicreader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lakshya.xkcdcomicreader.view.ComicGridFragment;
import com.lakshya.xkcdcomicreader.view.ComicListFragment;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            return new ComicListFragment();
        }
        else {
            return new ComicGridFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
