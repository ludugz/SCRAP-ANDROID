package eplus.scrap.common;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferences {
    private final static String PREF_DEVICE_TOKEN_KEY = "device_token_preference";
    public final static String PREF_KEY = "scan_preference";

    // key save user info
    // get string preference
    public static String getStringPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_KEY, 0)
                    .getString(strKey, "");
        } else {
            return null;
        }
    }

    public static int getIntPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_KEY, 0)
                    .getInt(strKey, -1);
        } else {
            return -1;
        }
    }

    public static boolean getBoolPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_KEY, 0)
                    .getBoolean(strKey, false);
        } else {
            return true;
        }
    }

    public static float getFloatPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_KEY, 0)
                    .getFloat(strKey, -1);
        } else {
            return -1;
        }
    }

    public static boolean saveStringPreference(Context pContext, String strKey,
                                               String strValue) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {

            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_KEY, 0)
                    .edit();
            e.putString(strKey, strValue);
            e.apply();
            return true;
        }
        return false;
    }

    public static boolean saveIntPreference(Context pContext, String strKey,
                                            int iValue) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {

            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_KEY, 0)
                    .edit();
            e.putInt(strKey, iValue);
            e.apply();
            return true;
        }
        return false;
    }

    public static boolean saveBooleanPreference(Context pContext,
                                                String strKey, boolean bValue) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_KEY, 0)
                    .edit();
            e.putBoolean(strKey, bValue);
            e.apply();
            return true;
        }
        return false;
    }

    public static boolean saveFloatPreference(Context pContext, String strKey,
                                              float fValue) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_KEY, 0)
                    .edit();
            e.putFloat(strKey, fValue);
            e.apply();
            return true;
        }
        return false;
    }

    public static boolean saveLongPrefrence(Context pContext,String strKey, long lValue){
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_KEY, 0)
                    .edit();
            e.putLong(strKey, lValue);
            e.apply();
            return true;
        }
        return false;
    }
    // get long preference
    public static long getLongPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_KEY, 0)
                    .getLong(strKey, -1);
        } else {
            return -1;
        }
    }
    // get string preference
    public static String getDeviceTokenPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0)
                    .getString(strKey, "");
        } else {
            return null;
        }

    }

    public static boolean saveDeviceTokenPreference(Context pContext, String strKey,
                                                    String strValue) {
        if (pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0) != null) {

            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0)
                    .edit();
            e.putString(strKey, strValue);
            e.apply();
            return true;
        }
        return false;
    }

    public static int getAppVersionPreference(Context pContext, String strKey) {
        if (pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0) != null) {
            return pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0)
                    .getInt(strKey, -1);
        } else {
            return -1;
        }

    }
    public static boolean saveAppVersionPreference(Context pContext, String strKey,
                                                   int iValue) {
        if (pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0) != null) {

            SharedPreferences.Editor e = pContext.getSharedPreferences(PREF_DEVICE_TOKEN_KEY, 0)
                    .edit();
            e.putInt(strKey, iValue);
            e.apply();
            return true;
        }
        return false;
    }
    public static void removeKeySharedPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
