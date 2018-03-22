package eplus.scrap.common;

/**
 * Created by nals-anhdv on 9/24/17.
 */
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {
    public static void getListViewSize(ListView myListView, int height) {
        ListAdapter myListAdapter = myListView.getAdapter();

        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            if(listItem != null){
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = height*2/10 + totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount()));
        myListView.setLayoutParams(params);
    }
    public static void getListViewSize500(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height =50 + myListAdapter.getCount()*150 + totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        //Log.i("height of listItem:", String.valueOf(totalHeight));
    }
    public static void getListViewSize_none(ListView myListView, int height) {
        ListAdapter myListAdapter = myListView.getAdapter();

        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount()));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        //Log.i("height of listItem:", String.valueOf(totalHeight));
    }
}


