package eplus.scrap.view.tourdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import eplus.scrap.R;
import eplus.scrap.adapter.ProductTabAdapter;
import eplus.scrap.common.Helper;
import eplus.scrap.model.TourDetail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TourDetail.TabsBean mItemsBean;
    private int mPosition;
    private ListView mListView;
    private ProductTabAdapter productTabAdapter;
    private FragmentActivity myContext;

    private OnFragmentInteractionListener mListener;

    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }

    public PriceDetailFragment() {
        // Required empty public constructor
    }
    public static PriceDetailFragment newInstance(TourDetail.TabsBean tabsBean, int position) {
        PriceDetailFragment fragment = new PriceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, tabsBean);
        args.putInt(ARG_PARAM2,position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try{
                mItemsBean = getArguments().getParcelable(ARG_PARAM1);
                mPosition = getArguments().getInt(ARG_PARAM2);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        myContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_price_detail, container, false);
        mListView = view.findViewById(R.id.list_product_tap);
        LinearLayout viewHeader = new LinearLayout(getCurrentContext());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 200);
        viewHeader.setLayoutParams(lp);
        //mListView.addFooterView(viewHeader, null, false);
        productTabAdapter = new ProductTabAdapter(mItemsBean.getItems(),getCurrentContext(),mPosition);
        mListView.setAdapter(productTabAdapter);
        productTabAdapter.notifyDataSetChanged();
       // Helper.getListViewSize_none(mListView,0);

        return  view;
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
}
