package eplus.scrap.networking;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import eplus.scrap.common.CommonFunc;
import eplus.scrap.networking.Retrofit.Api;
import eplus.scrap.networking.Retrofit.Data;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static eplus.scrap.networking.ApiKey.SECRET_KEY;
import static eplus.scrap.networking.ApiKey.SECRET_TAG;

/**
 * Created by Dungpa
 * Date: 19/04/15
 */
public class BaseRestClient {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
     public static OkHttpClient mClient =  new OkHttpClient();//()
    public static Request request;

    public BaseRestClient() throws NoSuchAlgorithmException {
    }


    public static void getRetrofit(final Context pContext, final String url, final HashMap<String, String> params, final DataResponse response, boolean isShowLoading) {
        if (pContext != null) {
            if (isShowLoading)
                CommonFunc.showLoadingView(pContext);
            String languageToLoad = "ja";//Locale.getDefault().getLanguage(); // your language
            String fullURL = url;
            StringBuilder urlParams = new StringBuilder(url + "?");
            if (params != null) {

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value;
                    value = android.net.Uri.encode(entry.getValue(), "UTF-8");
                    urlParams.append(key).append("=").append(value).append("&");
                }
                fullURL = urlParams.toString();
                if (fullURL.length() > 0) {
                    fullURL = fullURL.substring(0, fullURL.length() - 1);
                }
            }
            fullURL = fullURL + "&lang_code=" + languageToLoad + "/";


            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            Api api = retrofit.create(Api.class);
            Call<Data> call = api.getRetrofitData(20,"1","coming_soon","ja");
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {

                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {

                }
            });

        }
    }


















    public static void get(final Context pContext, final String url, final HashMap<String, String> params, final HashMap<String, String> headers, final DataResponse response, boolean isShowLoading) {
        Log.i("tntan92","");
        if(pContext != null){
            if (isShowLoading)
                CommonFunc.showLoadingView(pContext);
            String languageToLoad = "ja";//Locale.getDefault().getLanguage(); // your language
            String fullURL = url;
            StringBuilder urlParams = new StringBuilder(url + "?");
            if (params != null) {

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value;
                    value = android.net.Uri.encode(entry.getValue(),"UTF-8");
                    urlParams.append(key).append("=").append(value).append("&");
                }
                fullURL = urlParams.toString();
                if (fullURL.length() > 0) {
                    fullURL = fullURL.substring(0, fullURL.length() - 1);
                }
            }
            fullURL = fullURL + "&lang_code="+languageToLoad;
            Request.Builder builder = new Request.Builder()
                    .url(fullURL);
            builder.addHeader(SECRET_TAG, SECRET_KEY);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addHeader(key, value);
                }
            }
            request = builder.build();
            mClient.newCall(request).enqueue(response);
        }
    }



    public static void get( Context pContext, final String url, final HashMap<String, String> params, final HashMap<String, String> headers, final DataResponse response) {
        String fullURL = url;
        StringBuilder urlParams = new StringBuilder(url + "?");
        if (params != null) {

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value;
                value = android.net.Uri.encode(entry.getValue(),"UTF-8");
                urlParams.append(key).append("=").append(value).append("&");
            }
            fullURL = urlParams.toString();
            if (fullURL.length() > 0) {
                fullURL = fullURL.substring(0, fullURL.length() - 1);
            }
        }
        Request.Builder builder = new Request.Builder()
                .url(fullURL);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }
        request = builder.build();
        mClient.newCall(request).enqueue(response);

    }
    public static void post(final Context pContext, final String url, final HashMap<String, String> params, HashMap<String, String> headers, final DataResponse response) {

        JSONObject objParams = new JSONObject(params);
        RequestBody requestBody = RequestBody.create(JSON, objParams.toString());
        if (CommonFunc.isNetworkConnected(pContext)) {
            CommonFunc.showLoadingView(pContext);
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-type", "application/json");
            builder.addHeader(SECRET_TAG, SECRET_KEY);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addHeader(key, value);
                }
            }
            request = builder.build();
            mClient.newCall(request).enqueue(response);
        }
    }

    public static void post(final Context pContext, final String url, final HashMap<String, String> params, HashMap<String, String> headers, final DataResponse response, boolean isShowLoadingView) {
        if(pContext != null){
            JSONObject objParams = new JSONObject(params);
            RequestBody requestBody = RequestBody.create(JSON, objParams.toString());
            if (CommonFunc.isNetworkConnected(pContext)) {
                Request.Builder builder = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("Content-type", "application/json");
                builder.addHeader(SECRET_TAG, SECRET_KEY);
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        builder.addHeader(key, value);
                    }
                }
                request = builder.build();

                mClient.newCall(request).enqueue(response);
            }
        }

    }
}
