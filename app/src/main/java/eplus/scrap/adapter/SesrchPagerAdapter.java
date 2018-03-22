package eplus.scrap.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import eplus.scrap.model.TourDetail;
import eplus.scrap.view.SearchComminsoonFragment;
import eplus.scrap.view.SearchOpennowFragment;


/**
 * Created by nals-anhdv on 3/28/17.
 */

public class SesrchPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Context context;
    private String mkeyword;
    private long mFromdate = 0;
    private long mToDate = 0;


    protected OnCompleteListener mListener;

    public interface OnCompleteListener {
        void onShowTour(TourDetail tourDetail);
        void onNointernet();
    }
    public SesrchPagerAdapter(OnCompleteListener listener,FragmentManager fragmentManager, int NumOfTabs, Context context,String keyword,long fromdate,long toDate) {
        super(fragmentManager);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
        this.mFromdate = fromdate;
        this.mToDate = toDate;
        this.mkeyword = keyword;
        this.mListener = listener;
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
            case 0 :
                return  SearchComminsoonFragment.newInstance(new SearchComminsoonFragment.OnCompleteListener() {
                    @Override
                    public void onShowTour(TourDetail tourDetail) {
                        mListener.onShowTour(tourDetail);

                    }

                    @Override
                    public void onNointernet() {
                        mListener.onNointernet();
                    }
                },mkeyword, mFromdate, mToDate);
            case 1 :
                return  SearchOpennowFragment.newInstance(new SearchOpennowFragment.OnCompleteListener() {
                    @Override
                    public void onShowTour(TourDetail tourDetail) {
                        mListener.onShowTour(tourDetail);
                    }

                    @Override
                    public void onNointernet() {
                        mListener.onNointernet();
                    }
                },mkeyword, mFromdate, mToDate);
            default:
                return null;
        }


    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

}
