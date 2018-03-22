package eplus.scrap.view.tourdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import eplus.scrap.R;
import eplus.scrap.model.TourDetail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayFormatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayFormatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFormatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TourDetail mTourDetail;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PlayFormatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment PlayFormatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayFormatFragment newInstance(TourDetail tourDetail) {
        PlayFormatFragment fragment = new PlayFormatFragment();
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
        View view = inflater.inflate(R.layout.fragment_play_format, container, false);
        WebView browser_howto = (WebView) view.findViewById(R.id.wv_how_to_play);
        String html = "";
//        if(mTourDetail.getTabs().getShow_how_to_play().equals("Y")) {
//            html = mTourDetail.getTabs().getHow_to_play();
//            browser_howto.getSettings().setJavaScriptEnabled(true);
//            browser_howto.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
//        }else {
//            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.line_how_to_play);
//            linearLayout.setVisibility(View.GONE);
//            browser_howto.setVisibility(View.GONE);
//        }
//
//        WebView browser_timelimit = (WebView) view.findViewById(R.id.wv_time_limit);
//        html = mTourDetail.getTabs().getTime_limit();
//        browser_timelimit.getSettings().setJavaScriptEnabled(true);
//        browser_timelimit.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
//
//        WebView browser_numberofppl = (WebView) view.findViewById(R.id.wv_number_of_ppl);
//        html = mTourDetail.getTabs().getNumber_of_ppl_per_team_how_to();
//        browser_numberofppl.getSettings().setJavaScriptEnabled(true);
//        browser_numberofppl.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

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
