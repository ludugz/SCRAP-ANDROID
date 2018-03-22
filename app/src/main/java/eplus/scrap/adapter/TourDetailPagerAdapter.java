package eplus.scrap.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import eplus.scrap.model.TourDetail;


/**
 * Created by nals-anhdv on 3/28/17.
 */

public class TourDetailPagerAdapter extends FragmentPagerAdapter {
    TourDetail mTourDetail;
    private List<Fragment> fragments;

    public TourDetailPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments , TourDetail tourDetail) {
        super(fragmentManager);
        this.mTourDetail = tourDetail;
        this.fragments = fragments;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        fragments.set(position, createdFragment);
        return createdFragment;
    }
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {


        return this.fragments.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "" + position;
    }


    @Override
    public int getItemPosition(Object object) {
//        BoxOfficeFragment f = (BoxOfficeFragment ) object;
//        if (f != null) {
//            f.update(mTourDetail);
//        }
        return POSITION_NONE;
    }
}
