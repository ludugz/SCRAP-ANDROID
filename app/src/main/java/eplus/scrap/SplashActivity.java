package eplus.scrap;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Arrays;

import eplus.scrap.common.SharePreferences;
import eplus.scrap.view.WebViewFragment;
import io.fabric.sdk.android.Fabric;

import static eplus.scrap.Constants.SELECTION_COUPON_DETAIL;
import static eplus.scrap.Constants.SELECTION_EVENT;
import static eplus.scrap.Constants.SELECTION_NEWS_DETAIL;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */


    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    private String TAG = "MAIN_APP";

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    @Override
    protected void onStart() {
        super.onStart();
        ShortcutManager shortcutManager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            Intent intent1 = new Intent(getApplicationContext(), SplashActivity.class);
            intent1.putExtra("value",1);
            intent1.setAction(Intent.ACTION_VIEW);

            Intent intent2 = new Intent(getApplicationContext(), SplashActivity.class);
            intent2.putExtra("value",2);
            intent2.setAction(Intent.ACTION_VIEW);

            Intent intent3 = new Intent(getApplicationContext(), SplashActivity.class);
            intent3.putExtra("value",3);
            intent3.setAction(Intent.ACTION_VIEW);

            Intent intent4 = new Intent(getApplicationContext(), SplashActivity.class);
            intent4.putExtra("value",4);
            intent4.setAction(Intent.ACTION_VIEW);

            shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "myticket")
                    .setShortLabel(getString(R.string.my10_title))
                    .setLongLabel(getString(R.string.my10_title))
                    .setIcon(Icon.createWithResource(SplashActivity.this, R.drawable.icon_ticket_shortcut))
                    .setIntent(intent1)
                    .build();
            ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "coupon")
                    .setShortLabel(getString(R.string.coupon))
                    .setLongLabel(getString(R.string.coupon))
                    .setIcon(Icon.createWithResource(SplashActivity.this, R.drawable.icon_coupon_shortcut))
                    .setIntent(intent2)
                    .build();
            ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this, "news")
                    .setShortLabel(getString(R.string.new_text))
                    .setLongLabel(getString(R.string.new_text))
                    .setIcon(Icon.createWithResource(SplashActivity.this, R.drawable.icon_news_shortcut))
                    .setIntent(intent3)
                    .build();
            ShortcutInfo shortcut4 = new ShortcutInfo.Builder(this, "search")
                    .setShortLabel(getString(R.string.ct10_title))
                    .setLongLabel(getString(R.string.ct10_title))
                    .setIcon(Icon.createWithResource(SplashActivity.this, R.drawable.icon_search_shortcut))
                    .setIntent(intent4)
                    .build();

            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut2));
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut3));
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut4));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent1 = getIntent();
        setContentView(R.layout.activity_splash);

        SharePreferences.saveStringPreference(SplashActivity.this,"limit_startDate", "");
        SharePreferences.saveStringPreference(SplashActivity.this,"limit_endDate","");
        onNewIntent(getIntent());
        //Crashlytics.getInstance().crash();
//        Crashlytics.getInstance();
//        Fabric.with(this, new Crashlytics());
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                //.debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


    }
    protected void onNewIntent(Intent intent) {
        int selection = 0;
        String topic = "";
        String event_id = "";
        String coupon_type = "";
        int shortcut = 0;
        if(intent.getExtras() != null) {

            for (String key : intent.getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
                if(key.equals("deep_link")) {
                    String[] separated = value.toString().split("//");
                    String[] action = separated[1].split("/");
                    if(action[0].equals("news")) {
                        topic = action[1];
                        selection = SELECTION_NEWS_DETAIL;

                    } else if(action[0].equals("event")) {
                        event_id = action[1];
                        selection = SELECTION_EVENT;
                    } else if(action[0].equals("coupon")) {
                        coupon_type = action[1];
                        selection = SELECTION_COUPON_DETAIL;
                    }
                } else if(key.equals("value")) {
                    selection = (int)value;
                }
            }
        }
        final int finalSelection = selection;
        final String finalTopic = topic;
        final String finalEvent_id = event_id;
        final String finalCoupon_type = coupon_type;
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra("value", finalSelection);
                i.putExtra("topic", finalTopic);
                i.putExtra("event_id", finalEvent_id);
                i.putExtra("coupon_type", finalCoupon_type);
                //i.putExtra("value", finalShortcut);
                startActivity(i);
                // close this activity
                finish();
            }
        }, AUTO_HIDE_DELAY_MILLIS);

//        if (Intent.ACTION_VIEW.equals(action) && data != null) {
////            if (data.endsWith("note")) {
////                data = data.substring(0, data.lastIndexOf("/"));
////            }
////            String recipeId = data.substring(data.lastIndexOf("/") + 1);
//
//        } else
//        {
//            for (String key : intent.getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
//            }
//        }
    }
    private void loadwebwithurl(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewFragment fragment = WebViewFragment.newInstance(new WebViewFragment.OnCompleteListener() {
            @Override
            public void onComplete() {



            }
        }, url,false,false);
        fragment.show(ft, "webview");
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }


}
