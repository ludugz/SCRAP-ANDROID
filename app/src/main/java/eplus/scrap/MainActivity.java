package eplus.scrap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import eplus.scrap.adapter.MyPagerAdapter;
import eplus.scrap.common.LocaleHelper;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import eplus.scrap.view.MenuFragment;
import eplus.scrap.view.WebViewFragment;
import eplus.scrap.view.WebViewNEWSFragment;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.Constants.SELECTION_COUPON;
import static eplus.scrap.Constants.SELECTION_COUPON_DETAIL;
import static eplus.scrap.Constants.SELECTION_EVENT;
import static eplus.scrap.Constants.SELECTION_MYTICKET;
import static eplus.scrap.Constants.SELECTION_NEWS;
import static eplus.scrap.Constants.SELECTION_NEWS_DETAIL;
import static eplus.scrap.Constants.SELECTION_SEARCH;
import static eplus.scrap.Constants.TAB_MENU;
import static eplus.scrap.Constants.TAB_MYTICKET;
import static eplus.scrap.Constants.TAB_NEWS;
import static eplus.scrap.Constants.TAB_SEARCH;
import static eplus.scrap.Constants.TAB_TOP_PAGE;
import static eplus.scrap.networking.ApiKey.GET_LIMIT_DATE;
import static eplus.scrap.networking.ApiKey.URL_LOGIN;
import static eplus.scrap.networking.ApiKey.URL_NEWS_TOPIC;

//import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    public  TabLayout tabLayout;
    public  ImageView blueImage;
     MyPagerAdapter adapterViewPager;


    SharedPreferences prefs = null;
    int[] icon_unselect = { R.drawable.tp_news_icon, R.drawable.tp_search_icon, R.drawable.ic_tranparent,
            R.drawable.tp_myticket_icon, R.drawable.tp_menu_icon
             };
    int[] icon_select = { R.drawable.tp_news_icon_yl, R.drawable.tp_search_icon_yl, R.drawable.ic_tranparent,
            R.drawable.tp_myticket_icon_yl, R.drawable.tp_menu_icon_yl
    };
    private MenuFragment.OnFragmentInteractionListener mListener;
    private int mPosition;
    private  ViewPager vpPager;
    private int width;
    private Button btlgin;
    private LinearLayout btcancel;
    private RelativeLayout lgview;
    private Button bt_home;

    public void showTabLayout() {
        tabLayout.setVisibility(View.VISIBLE);
        blueImage.setVisibility(View.VISIBLE);
    }
    public  void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
        blueImage.setVisibility(View.GONE);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        }
        StrictMode.setThreadPolicy(policy);

        Intent intent1 = getIntent();
        int value = intent1.getIntExtra("value",0);

        bt_home = findViewById(R.id.bt_home);
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition = 2;
                vpPager.setCurrentItem(mPosition);
                tabselect(mPosition);
            }
        });

        blueImage = findViewById(R.id.blue_bg);
        blueImage.setImageResource(R.drawable.tabmain_bg_highligh);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getLayoutParams().height = (int) (width * 0.19);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.new_text)).setIcon(R.drawable.tp_news_icon));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ct10_title)).setIcon(R.drawable.tp_search_icon));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tp10_title)).setIcon(R.drawable.ic_tranparent));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.my10_title)).setIcon(R.drawable.tp_myticket_icon));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.mn10_title)).setIcon(R.drawable.tp_menu_icon));
        mPosition = TAB_TOP_PAGE;
        String topic = "";
        String event_id = "";
        String coupon_type = "";

        if(value == SELECTION_MYTICKET) {
            mPosition = TAB_MYTICKET;
        } else if(value == SELECTION_COUPON ) {
            mPosition = TAB_MENU;
        } else if(value == SELECTION_NEWS ) {
            mPosition = TAB_NEWS;
        } else if(value == SELECTION_SEARCH) {
            mPosition = TAB_SEARCH;
        } else if (value == SELECTION_NEWS_DETAIL) {
            if(!intent1.getStringExtra("topic").isEmpty()) {
                String url = URL_NEWS_TOPIC + intent1.getStringExtra("topic");
                loadwebNEWSwithurl(url);
                mPosition = TAB_NEWS;
            }
        } else if (value == SELECTION_EVENT) {
            if(!intent1.getStringExtra("event_id").isEmpty()) {
                event_id = intent1.getStringExtra("event_id");
            }
        } else if(value == SELECTION_COUPON_DETAIL) {
            if(!intent1.getStringExtra("coupon_type").isEmpty()) {
             coupon_type =  intent1.getStringExtra("coupon_type");
                mPosition = TAB_MENU;
            }
        }
        else mPosition = TAB_TOP_PAGE;

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        vpPager = findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),this,value,topic,event_id,coupon_type);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        vpPager.setCurrentItem(mPosition);
        vpPager.setOffscreenPageLimit(5);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    mPosition = tab.getPosition();
                    vpPager.setCurrentItem(tab.getPosition());
                    tabselect(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getStartEndTime();
        RelativeLayout re_tabbar_bg = findViewById(R.id.tab_bg);
        re_tabbar_bg.getLayoutParams().height = (int) (width * 0.309);
        re_tabbar_bg.getLayoutParams().width = width;
        initlogin();
        initTabbar(mPosition);
    }
    private void loadwebNEWSwithurl(String url) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
            @Override
            public void onComplete() {

            }
        }, url,false);
        fragment.show(ft, "webview");
    }
    private void initlogin() {
        lgview = findViewById(R.id.re_login);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        ImageView img_header = findViewById(R.id.my_awesome_toolbar);
        img_header.getLayoutParams().width = width;
        img_header.getLayoutParams().height = (int) (width * 0.4173);
        btlgin = findViewById(R.id.bt_login);
        btlgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                @Override
                public void onComplete() {
                    if (prefs.getBoolean("firstrun", true)) {
                        prefs.edit().putBoolean("firstrun", false).commit();
                    }
                    if (!SharePreferences.getStringPreference(MainActivity.this, "token").isEmpty()) {
                        lgview.setVisibility(View.GONE);
                    }
                }
                }, URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
                fragment.show(ft, "webview");
            }
        });
        btcancel = findViewById(R.id.bt_cancel);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lgview.setVisibility(View.GONE);
                if (prefs.getBoolean("firstrun", true)) {
                    prefs.edit().putBoolean("firstrun", false).commit();
                }


            }
        });
        TextView textView = findViewById(R.id.tv_cancel);
        SpannableString spanString = new SpannableString("今は登録しない");
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spanString.length(), 0);
        textView.setText(spanString);
    }

    private void initTabbar (int position) {
        if(position == 2) {
            blueImage.setImageResource(R.drawable.tabmain_bg_highligh);
        }
        else {
            blueImage.setImageResource(R.drawable.tabmain_bg);
        }
        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        TextView tv_text_tabOne = tabOne.findViewById(R.id.tv_tabbar);
        ImageView icon_tab_tabOne = tabOne.findViewById(R.id.icon_tabbar);
        tv_text_tabOne.setText(getString(R.string.new_text));
        if (position == 0) {
            tv_text_tabOne.setTextColor(Color.YELLOW);
            icon_tab_tabOne.setImageDrawable(getResources().getDrawable(R.drawable.tp_news_icon_yl));
        } else {
            icon_tab_tabOne.setImageDrawable(getResources().getDrawable(R.drawable.tp_news_icon));
            tv_text_tabOne.setTextColor(Color.WHITE);
        }
        RelativeLayout line_content_tap = tabOne.findViewById(R.id.line_content_tab);
        line_content_tap.getLayoutParams().height = (int) (width * 0.19);
        icon_tab_tabOne.getLayoutParams().height = (int) (width * 0.05733);
        icon_tab_tabOne.getLayoutParams().width = (int) (width * 0.1);
        ViewGroup.MarginLayoutParams iconTab_tabOne_params = (ViewGroup.MarginLayoutParams) icon_tab_tabOne.getLayoutParams();
        iconTab_tabOne_params.topMargin = (int) (width * 0.03466);
        ViewGroup.MarginLayoutParams tv_text_tabOne_params = (ViewGroup.MarginLayoutParams) tv_text_tabOne.getLayoutParams();
        tv_text_tabOne_params.bottomMargin = (int) (width * 0.038);


        RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        TextView tv_text_tabTwo = tabTwo.findViewById(R.id.tv_tabbar);
        tv_text_tabTwo.setText(getString(R.string.ct10_title));
        ImageView icon_tab_tabTwo = tabTwo.findViewById(R.id.icon_tabbar);
        icon_tab_tabTwo.setImageDrawable(getResources().getDrawable(R.drawable.tp_search_icon));
        if(position == 1) {
            tv_text_tabTwo.setTextColor(Color.YELLOW);
            icon_tab_tabTwo.setImageDrawable(getResources().getDrawable(R.drawable.tp_search_icon_yl));
        }
        RelativeLayout line_content_tabTwo = tabTwo.findViewById(R.id.line_content_tab);
        line_content_tabTwo.getLayoutParams().height = (int) (width * 0.19);
        icon_tab_tabTwo.getLayoutParams().height = (int) (width * 0.05733);
        icon_tab_tabTwo.getLayoutParams().width = (int) (width * 0.1);
        ViewGroup.MarginLayoutParams iconTab_tabTwo_params = (ViewGroup.MarginLayoutParams) icon_tab_tabTwo.getLayoutParams();
        iconTab_tabTwo_params.topMargin = (int) (width * 0.03466);
        ViewGroup.MarginLayoutParams tv_text_tabTwo_params = (ViewGroup.MarginLayoutParams) tv_text_tabTwo.getLayoutParams();
        tv_text_tabTwo_params.bottomMargin = (int) (width * 0.038);

        RelativeLayout tabThree = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        RelativeLayout line_content_tabThree = tabThree.findViewById(R.id.line_content_tab);
        line_content_tabThree.getLayoutParams().height = (int) (width * 0.19);
        ImageView icon_tab_tabThree = tabThree.findViewById(R.id.icon_tabbar);
        icon_tab_tabThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_tranparent));
        icon_tab_tabThree.getLayoutParams().height = (int) (width * 0.05733);
        icon_tab_tabThree.getLayoutParams().width = (int) (width * 0.1);
        ViewGroup.MarginLayoutParams iconTab_tabThree_params = (ViewGroup.MarginLayoutParams) icon_tab_tabThree.getLayoutParams();
        iconTab_tabThree_params.topMargin = (int) (width * 0.03466);
        TextView tv_text_tabThree = tabThree.findViewById(R.id.tv_tabbar);
        tv_text_tabThree.setText(getString(R.string.tp10_title));
        ViewGroup.MarginLayoutParams tv_text_tabThree_params = (ViewGroup.MarginLayoutParams) tv_text_tabThree.getLayoutParams();
        tv_text_tabThree_params.bottomMargin = (int) (width * 0.038);
        if(position == 2) {
            tv_text_tabThree.setTextColor(Color.YELLOW);
        }

        RelativeLayout tabFour = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        RelativeLayout line_content_tabFour = tabFour.findViewById(R.id.line_content_tab);
        line_content_tabFour.getLayoutParams().height = (int) (width * 0.19);
        ImageView icon_tab_tabFour = tabFour.findViewById(R.id.icon_tabbar);
        icon_tab_tabFour.setImageDrawable(getResources().getDrawable(R.drawable.tp_myticket_icon));
        icon_tab_tabFour.getLayoutParams().height = (int) (width * 0.05733);
        icon_tab_tabFour.getLayoutParams().width = (int) (width * 0.1);
        ViewGroup.MarginLayoutParams iconTab_tabFour_params = (ViewGroup.MarginLayoutParams) icon_tab_tabFour.getLayoutParams();
        iconTab_tabFour_params.topMargin = (int) (width * 0.03466);
        TextView tv_text_tabFour = tabFour.findViewById(R.id.tv_tabbar);
        tv_text_tabFour.setText(getString(R.string.my10_title));
        ViewGroup.MarginLayoutParams tv_text_tabFour_params = (ViewGroup.MarginLayoutParams) tv_text_tabFour.getLayoutParams();
        tv_text_tabFour_params.bottomMargin = (int) (width * 0.038);
        if(position == 3) {
            tv_text_tabFour.setTextColor(Color.YELLOW);
            icon_tab_tabFour.setImageDrawable(getResources().getDrawable(R.drawable.tp_myticket_icon_yl));
        }

        RelativeLayout tabFive = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        RelativeLayout line_content_tabFive = tabFive.findViewById(R.id.line_content_tab);
        line_content_tabFive.getLayoutParams().height = (int) (width * 0.19);
        ImageView icon_tab_tabFive = tabFive.findViewById(R.id.icon_tabbar);
        icon_tab_tabFive.setImageDrawable(getResources().getDrawable(R.drawable.tp_menu_icon));
        icon_tab_tabFive.getLayoutParams().height = (int) (width * 0.05733);
        icon_tab_tabFive.getLayoutParams().width = (int) (width * 0.1);
        ViewGroup.MarginLayoutParams iconTab_tabFive_params = (ViewGroup.MarginLayoutParams) icon_tab_tabFive.getLayoutParams();
        iconTab_tabFive_params.topMargin = (int) (width * 0.03466);
        TextView tv_text_tabFive = tabFive.findViewById(R.id.tv_tabbar);
        tv_text_tabFive.setText(getString(R.string.mn10_title));
        ViewGroup.MarginLayoutParams tv_text_tabFive_params = (ViewGroup.MarginLayoutParams) tv_text_tabFive.getLayoutParams();
        tv_text_tabFive_params.bottomMargin = (int) (width * 0.038);
        if(position == 4) {
            tv_text_tabFive.setTextColor(Color.YELLOW);
            icon_tab_tabFive.setImageDrawable(getResources().getDrawable(R.drawable.tp_menu_icon_yl));
        }

        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        tabLayout.getTabAt(3).setCustomView(tabFour);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    private void tabselect (int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View view=tab.getCustomView();
            TextView tv_text = view.findViewById(R.id.tv_tabbar);
            ImageView icon_tab = view.findViewById(R.id.icon_tabbar);
            if(i == position) {
                tv_text.setTextColor(Color.YELLOW);
                icon_tab.setImageResource(icon_select[i]);
            } else {
                tv_text.setTextColor(Color.WHITE);
                icon_tab.setImageResource(icon_unselect[i]);
            }
            if(position == 2) {
                blueImage.setImageResource(R.drawable.tabmain_bg_highligh);
            }
            else {
                blueImage.setImageResource(R.drawable.tabmain_bg);
            }

        }

    }

    private void getStartEndTime() {
        HashMap<String, String> param = new HashMap<>();
        param.put("mode","start_end_calendar");

        DataResponse responseVeryfy = new DataResponse(MainActivity.this) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            JSONObject data =  jObject.getJSONObject("data");
                            SharePreferences.saveStringPreference(MainActivity.this,"limit_startDate", data.getString("start"));
                            SharePreferences.saveStringPreference(MainActivity.this,"limit_endDate",data.getString("end"));
                        }
                    }
                } catch (Exception e) {e.printStackTrace();

                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };

        BaseRestClient.get(MainActivity.this, GET_LIMIT_DATE, param, null, responseVeryfy, false);
    }



    @Override
    protected void onResume() {
        super.onResume();


        if (prefs.getBoolean("firstrun", true)) {
            lgview.setVisibility(View.VISIBLE);
        }
        else {
            lgview.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
