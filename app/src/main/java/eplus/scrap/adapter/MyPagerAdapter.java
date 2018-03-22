package eplus.scrap.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import eplus.scrap.view.MenuFragment;
import eplus.scrap.view.MyTicketTabFragment;
import eplus.scrap.view.NEWSFragment;
import eplus.scrap.view.ScheduleFragment;
import eplus.scrap.view.SearchFragment;
import eplus.scrap.view.WebViewFragment;


/**
 * Created by nals-anhdv on 3/28/17.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    private Context context;
    private WebViewFragment fragment;
    private MyInterface listener ;
    int mValue;
    String topic;
    String event_id;
    String coupon_type;
    //private SearchFragment searchFragment = new  SearchFragment();
    public interface MyInterface {
        void myAction() ;
    }
    public void setListener(MyInterface listener) {
        this.listener = listener ;
    }

    public MyPagerAdapter(FragmentManager fragmentManager, int NumOfTabs, Context context, int value,String topic,
            String event_id,
            String coupon_type) {
        super(fragmentManager);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
        this.mValue = value;
        this.event_id = event_id;
        this.coupon_type = coupon_type;
        this.topic = topic;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
//        Log.d("MyPaper","value:"+mValue);
        switch (position) {
            case 0 :
                return NEWSFragment.newInstance(topic, "");
            case 1 :
                return SearchFragment.newInstance(mValue,"");
            case 2 :
                return ScheduleFragment.newInstance(event_id, "");
            case 3 :
                return  MyTicketTabFragment.newInstance();
            case 4:
                return MenuFragment.newInstance(mValue, coupon_type);
            default:
                return null;
        }


    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
