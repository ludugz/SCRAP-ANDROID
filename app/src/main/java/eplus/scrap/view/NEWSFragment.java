package eplus.scrap.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import eplus.scrap.R;
import eplus.scrap.adapter.NewsAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.rss.RSSFeed;
import eplus.scrap.common.rss.RSSItem;
import eplus.scrap.common.rss.RSSReader;
import eplus.scrap.common.rss.RSSReaderException;

import static eplus.scrap.networking.ApiKey.RSS_LINK;
import static eplus.scrap.networking.ApiKey.URL_NEWS_TOPIC;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NEWSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NEWSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NEWSFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mNewsId;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ArrayList<RSSItem> mListItem;
    private ListView mListView;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private LinearLayout lineErrMsg;
    private FragmentActivity myContext;

    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public NEWSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param new_id Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NEWSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NEWSFragment newInstance(String new_id, String param2) {
        NEWSFragment fragment = new NEWSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new_id);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        lineErrMsg = view.findViewById(R.id.line_nodata);
        lineErrMsg.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.load_progress);
        mListView = view.findViewById(R.id.lv_new);
        LinearLayout viewHeader = new LinearLayout(getCurrentContext());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 200);
        viewHeader.setLayoutParams(lp);
        mListView.addFooterView(viewHeader, null, false);
        swipeContainer = view.findViewById(R.id.swiperefresh_new);
        Boolean[] myTaskParams = { true, true, true };
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonFunc.isNetworkConnected(getCurrentContext())) {
                    new MyAsyncTask().execute(true);
                } else {
                    reNointernet.setVisibility(View.VISIBLE);
                }
            }
        });
        reNointernet = view.findViewById(R.id.re_no_internet_layout);
        reNointernet.setVisibility(View.GONE);
        lineBtRetry = view.findViewById(R.id.line_bt_retry);
        lineBtRetry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    lineBtRetry.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            lineBtRetry.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    lineBtRetry.getBackground().clearColorFilter();
                    if(CommonFunc.isNetworkConnected(getActivity())) {
                        new MyAsyncTask().execute(true);
                        reNointernet.setVisibility(View.GONE);
                    }
                    //searchtours(null,"", CommonFunc.getDate(mFromDate,"yyyy/MM/dd"),CommonFunc.getDate(mToDate,"yyyy/MM/dd"),mKeyword,"open_now",true);
                    //reNointernet.setVisibility(View.GONE);
                }
                return true;
            }
        });

        if(mListItem != null && mListItem.size() >0 ){
            progressBar.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
            updateData();
        }
        else {
            if(CommonFunc.isNetworkConnected(getActivity())) {
                new MyAsyncTask().execute(true);
            } else {
                reNointernet.setVisibility(View.VISIBLE);
            }
        }
        if(!mNewsId.isEmpty()){
            String url = URL_NEWS_TOPIC + mNewsId;
            shownews(url);
        }
        return  view;
    }
    private void shownews(String url) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewNEWSFragment fragment = WebViewNEWSFragment.newInstance(new WebViewNEWSFragment.OnCompleteListener() {
            @Override
            public void onComplete() {

            }
        }, url,false);
        fragment.show(ft, "webview");
    }
    private void updateData() {
        newsAdapter = new NewsAdapter(mListItem,getCurrentContext());
        mListView.setAdapter(newsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RSSItem rssItem = mListItem.get(position);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = WebViewNEWSFragment.newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, rssItem.getLink().toString(),false);
                fragment.show(ft, "webview");
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private class MyAsyncTask extends AsyncTask<Boolean, Integer, ArrayList<RSSItem>  > {

        ArrayList<RSSItem> mylist = new ArrayList<RSSItem>();

        @Override
        protected ArrayList<RSSItem> doInBackground(Boolean... params) {
            if(params[0])
                if (CommonFunc.isNetworkConnected(getCurrentContext())) {
                    try {
                        //progressBar.setVisibility(View.VISIBLE);
                        RSSReader reader = new RSSReader();
                        String uri = RSS_LINK;
                        try {
                            RSSFeed feed = reader.load(uri);
                            mylist.addAll(feed.getItems());
                            return mylist;

                        } catch (RSSReaderException e) {
                            e.printStackTrace();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RSSItem> result) {
            progressBar.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);
            if(result != null && result.size() >0) {
                lineErrMsg.setVisibility(View.GONE);
                mListItem = new ArrayList<>(result);
                newsAdapter = new NewsAdapter(result, getCurrentContext());
                mListView.setAdapter(newsAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RSSItem rssItem = mListItem.get(position);
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                            @Override
                            public void onComplete() {

                            }
                        }, rssItem.getLink().toString(), false);
                        fragment.show(ft, "webview");
                    }
                });
                lineErrMsg.setVisibility(View.GONE);
                swipeContainer.setVisibility(View.VISIBLE);
            } else {
                //mListView.setVisibility(View.GONE);
                swipeContainer.setVisibility(View.GONE);
                lineErrMsg.setVisibility(View.VISIBLE);
            }


        }
    }
}
