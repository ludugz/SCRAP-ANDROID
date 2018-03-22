package eplus.scrap;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import com.facebook.FacebookSdk;

import eplus.scrap.common.LocaleHelper;

/**
 * Created by nals-anhdv on 8/4/17.
 */

public class App extends Application {
    private Bitmap myBitmap;


    private static final String TAG = App.class.getSimpleName();
    public void onCreate(){
        super.onCreate();
        SmartAsyncPolicyHolder.INSTANCE.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());



    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ja"));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }
}
