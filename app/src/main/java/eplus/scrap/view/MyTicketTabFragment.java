package eplus.scrap.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import eplus.scrap.R;
import eplus.scrap.adapter.MyTicketPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyTicketTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyTicketTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTicketTabFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "ANH DOAN" ;

    // TODO: Rename and change types of parameters
    FragmentActivity myContext;
    MyTicketPagerAdapter adapterViewPager;
    private String mURL;
    //private WebView mWebview;
    private LinearLayout lineContentTicket;
    private  ViewPager vpPager;
    private TabLayout tabLayout;
    static MyTicketTabFragment fragment;

    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public MyTicketTabFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyTicketTabFragment newInstance() {
        MyTicketTabFragment fragment = new MyTicketTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
        myContext = getActivity();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myticket_tab, container, false);
        lineContentTicket = view.findViewById(R.id.line_content_ticket);
        tabLayout = view.findViewById(R.id.tabLayout_tourdetail);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ticket_can_be_use)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ticket_invalid)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        vpPager = view.findViewById(R.id.viewpager_detail);
        adapterViewPager = new MyTicketPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), getCurrentContext(), new MyTicketPagerAdapter.OnCompleteListener() {
            @Override
            public void onRefresh() {
                if(vpPager != null) {
                    vpPager.setAdapter(adapterViewPager);
                    adapterViewPager.notifyDataSetChanged();
                }
            }
        });
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        vpPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

         myContext = (FragmentActivity) context;
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void onActivityCreated(Bundle savedInstanceState1) {
        super.onActivityCreated(savedInstanceState1);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


//    class LoginInterface {
//        Context mContext;
//
//        /** Instantiate the interface and set the context */
//        LoginInterface(Context c) {
//            mContext = c;
//        }
//
//        private void updateView(){
//            getCurrentContext().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//        }
//        /** Show a toast from the web page */
//        @JavascriptInterface
//        public void showToast(String toast) {
//            Log.d("token:",toast);
//            SharePreferences.saveStringPreference(mContext,"token",toast/*"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjMiLCJ0aW1lIjoxNTAzMzg3NjY4fQ.nzLVxzelhbg-mF5c9KOoMJ2BRQGp--GGEy9345u20X0"*/);
//            updateView();
//            getCurrentContext().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    MainActivity.notifyTabLayout();
//
//                }
//            });
//
//
//
//        }
//    }
}
