package eplus.scrap.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import eplus.scrap.view.MyTicketFragment;
import eplus.scrap.view.MyTicketUsedFragment;


/**
 * Created by nals-anhdv on 3/28/17.
 */

public class MyTicketPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Context context;
    OnCompleteListener mListenter;
    public interface OnCompleteListener {
        void onRefresh();
    }
    public MyTicketPagerAdapter(FragmentManager fragmentManager, int NumOfTabs, Context context, OnCompleteListener listener) {
        super(fragmentManager);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
        this.mListenter = listener;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : {
                return  MyTicketFragment.newInstance(new MyTicketFragment.OnCompleteListener() {
                    @Override
                    public void onRefresh() {
                        mListenter.onRefresh();
                    }
                });
            }
            case 1 :
                return  MyTicketUsedFragment.newInstance(new MyTicketUsedFragment.OnCompleteListener() {
                    @Override
                    public void onRefresh() {
                        mListenter.onRefresh();
                    }
                });

            default:
                return null;
        }


    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "" + position;
    }



}
