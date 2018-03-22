package eplus.scrap.common;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.util.Locale;

/**
 * Created by nals-anhdv on 8/4/17.
 */

public class LocaleUtils {

    private static Locale sLocale;

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if(sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if(sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);


        }
    }

    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            // We must use the now-deprecated config.locale and res.updateConfiguration here,
            // because the replacements aren't available till API level 24 and 17 respectively.
            config.locale = sLocale;
            app.getBaseContext().getResources().updateConfiguration(config,
                    app.getBaseContext().getResources().getDisplayMetrics());
        }
    }
}