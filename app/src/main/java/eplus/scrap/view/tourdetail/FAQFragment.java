package eplus.scrap.view.tourdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import eplus.scrap.R;
import eplus.scrap.model.TourDetail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FAQFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAQFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TourDetail mTourDetail;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FAQFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FAQFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQFragment newInstance(TourDetail tourDetail) {
        FAQFragment fragment = new FAQFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, tourDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourDetail = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        WebView browser_faq = view.findViewById(R.id.wv_faq);
        WebSettings settings = browser_faq.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        browser_faq.setFocusable(true);
        //browser_faq.loadUrl(mTourDetail.getTab_faq());
        browser_faq.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        browser_faq.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler, String host, String realm) {
                handler.proceed("scrap_test", "scrap_test");
            }


            public void onPageFinished(WebView view, String url) {
                //Log.i("FAQ", "Finished loading URL: " + url);
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Log.e("FAQ", "Error: " + description);

            }
        });
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
}
