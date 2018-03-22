package eplus.scrap.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import eplus.scrap.R;


public class CommonFunc {
    public static final String DATE_FORMAT = "yyyy/MM/dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_NONE_HOUR = "yyyy/MM/dd";
    public static final String HOUR_FORMAT = "HH:mm";
    public static final int COLOR_HIGHLIGH = Color.parseColor("#C0C0C0");

    private static ProgressDialog mProgressDialog;
    private static AlertDialog mAlertDialog;
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
    public static String getTextHtml(String content) {
//        String htmlBody = "<html>\n" +
//                "    <head>\n" +
//                "        <meta charset=\"UTF-8\">\n" +
//                "        <style>a,abbr,acronym,address,applet,article,aside,audio,b,big,blockquote,body,canvas,caption,center,cite,code,dd,del,details,dfn,div,dl,dt,em,embed,fieldset,figcaption,figure,footer,form,h1,h2,h3,h4,h5,h6,header,hgroup,html,i,iframe,img,ins,kbd,label,legend,li,mark,menu,nav,object,ol,output,p,pre,q,ruby,s,samp,section,small,span,strike,strong,sub,summary,sup,table,tbody,td,tfoot,th,thead,time,tr,tt,u,ul,var,video{margin:0;padding:0;border:0;font:inherit;vertical-align:baseline}article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{display:block}body{line-height:1}ol,ul{list-style:none}blockquote,q{quotes:none}blockquote:after,blockquote:before,q:after,q:before{content:'';content:none}table{border-collapse:collapse;border-spacing:0}</style>\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                    content+ "\n" +
//                "    </body> \n" +
//                "</html>";
        String html = "<style> " + content + " {color: black;font-size: 0.37cm;}</style>";
        return html;
    }

    public static void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void showPopupDialog(Context context, String title, String message, String titlebutton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(titlebutton,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        pShowDialog(builder);
        final Button positiveButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public static void showDialogOneButton(Context context, String title, String message) {
        if (context == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", null);

        pShowDialog(builder);
    }

    private static void pShowDialog(AlertDialog.Builder builder) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        } else if (mAlertDialog != null) {
            mAlertDialog = null;
        }
        mAlertDialog = builder.create();
        mAlertDialog.show();
        TextView messageView = mAlertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        final Button positiveButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getDeviceName() {
        String deviceName = "";
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        if (myDevice != null)
            deviceName = myDevice.getName();
        return deviceName;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Coult not get package name: " + e);
        }
    }

    public static String encodeTextUTF8(String text) {
        String textEncoded = "";
        try {
            textEncoded = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return textEncoded;
    }

    public static String decodeTextUTF8(String text) {
        if (text == null) text = "";
        String textDecoded = "";
        try {
            textDecoded = (URLDecoder.decode(text, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return textDecoded;
    }

    public static String encodeBase64(String text) {
        if (text == null) text = "";
        String textEnCode;
        byte[] data;
        try {
            data = text.getBytes("UTF-8");
            textEnCode = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
        return textEnCode;
    }

    public static String decodeBase64(String text) {
        if (text == null) text = "";
        String textDecoded;
        try {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            textDecoded = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
        return textDecoded;
    }


    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void showLoadingView(Context context) {

        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(context);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    public static void hideLoadingView() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            if(dialog != null && dialog.getWindow() != null){
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.progressdialog);
            }

        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return dialog;
    }

    public static String convertArrayIntToString(ArrayList<Integer> list) {
        if (list == null) return "";
        if (list.size() > 0) {
            StringBuilder categories = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                int category = list.get(i);
                categories.append(category).append(",");
            }
            return categories.toString().substring(0, categories.length() - 1);
        }
        return "";
    }

    public static Bitmap rotateBitmap(Bitmap source, int angel) {
        Bitmap result;
        Matrix matrix = new Matrix();
        if ((angel == ExifInterface.ORIENTATION_ROTATE_180)) {
            matrix.postRotate(180);
            result = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } else if (angel == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
            result = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } else if (angel == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
            result = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } else {
            return source;
        }
        return result;
    }

    public static int getOrientation(String path) {
        int exifOrientation;
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
            exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
        } catch (IOException e1) {
            return -1;
        }
        return exifOrientation;
    }

    public static float convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static void destroy() {
        mProgressDialog = null;
        mAlertDialog = null;
    }

    /**
     * @param context
     * @param uri
     * @return
     */
    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    public static String getUuid(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return tManager.getDeviceId();
    }

    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public static String getNetworkInfomation(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return tm.getLine1Number();
    }

    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return tm.getDeviceId();
    }

    public static boolean isWifiEnable(Context context) {
        ConnectivityManager conMan = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
    }

    public static String getVersionName(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo != null ? "v" + pInfo.versionName : null;
    }

    public static int getVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo != null ? pInfo.versionCode : 0;
    }

    //open Map to view a location
    public static void openMap(String address, Activity pActivity) {
        Uri gmmIntentUri = Uri.parse("geo:0,0" + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(pActivity.getPackageManager()) != null) {
            pActivity.startActivity(mapIntent);
        } else {
            String url = "http://maps.google.com/maps?q=" + address;
            openBrowser(url, pActivity);
        }
    }

    public static void openBrowser(String url, Activity pActivity) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        pActivity.startActivity(browserIntent);
    }

    public static String getTextPrice(int price, String taxRate) {
        double fTaxRate = 0.08;
        if (!TextUtils.isEmpty(taxRate)) {
            try {
                fTaxRate = Double.parseDouble(taxRate) * 0.01;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double totalTax = price * fTaxRate + price;
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        //税込
        return "¥" + format.format(price) + " (税込: ¥" + format.format((int) Math.round(totalTax)) + ")";
    }


    public static String getDateFormat(String date) {
        //2016-09-08
        SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN);
        String reformattedStr = "";
        //2016年8月23日（火）
        try {
            reformattedStr = toFormat.format(fromFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static boolean isKanjiString(String input) {
        Set<Character.UnicodeBlock> kanjiUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
            add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
            add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.KATAKANA);
            add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
        }};
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!kanjiUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isKanJi(String input) {
        Set<Character.UnicodeBlock> kanjiUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
            add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
            add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
        }};
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!kanjiUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHiraganaString(String input) {
        Set<Character.UnicodeBlock> hiraganaUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
            add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
        }};
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!hiraganaUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
                return false;
            }

        }

        return true;
    }

    public static boolean validateTelephony(String tel) {
        if (TextUtils.isEmpty(tel)) {
            return false;
        }
        if (!(tel.length() == 11 || tel.length() == 10)) {
            return false;
        }
        if (tel.charAt(0) != '0') {
            return false;
        }
        for (Character c : tel.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        if(milliSeconds == 0 ) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static boolean commpareCurrentDate(String str1,String str2) {
        try{

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


            Date date1 = formatter.parse(str1);
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2)<0)
            {
                System.out.println("date2 is Greater than my date1");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public static String getTime(String time){
        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH:mm");
        try {
            Date date = df.parse(time);
            return formatter_to.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    // method for bitmap to base64
    public static String bitmapEncodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
    // method for base64 to bitmap
    public static Bitmap bitmapDecodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
