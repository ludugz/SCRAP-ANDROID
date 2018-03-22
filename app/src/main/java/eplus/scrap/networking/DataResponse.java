package eplus.scrap.networking;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import eplus.scrap.common.CommonFunc;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DataResponse implements Callback {
    private Context mContext;
    private boolean mIsHideLoadingView = true;
    private boolean mIsShowErrorDialog = true;

    protected DataResponse(Context pContext) {
        mContext = pContext;
    }

    public DataResponse(Context pContext, boolean isHideLoadingView) {
        mContext = pContext;
        mIsHideLoadingView = isHideLoadingView;
    }
    public void onFailure(Request request, IOException e) {
//        Log.d("get fail","");
        if (!(mContext instanceof Activity)) return;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonFunc.hideLoadingView();

            }
        });
    }
    protected void response(final Response response) {
        if (mContext == null || !(mContext instanceof Activity)) return;
        ((Activity) mContext).runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mIsHideLoadingView)
                        if (!response.isSuccessful()) {
                            try {
                                String msg = response.body().string();
                                JSONObject jsonResponse = new JSONObject(msg);
                                if (jsonResponse.isNull(ApiKey.MESSAGE))
                                    showDialogUpdateError(null);
                                else
                                    showDialogUpdateError(jsonResponse.getString(ApiKey.MESSAGE));
                                onNotSuccess();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        try {
                            onRealSuccess(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void on406() {
    }

    public void onRealSuccess(String response) {
//        Log.d("DucNT", response);
    }
    public void onRealFail() {

    }

    protected void onNotSuccess() {
    }

    protected void onNotFound() {
    }
    protected void onExpriredToken(){

    }
    protected boolean isHideLoadingView() {
        return mIsHideLoadingView;
    }

    public void isShowErrorDialog(boolean isShow) {
        mIsShowErrorDialog = isShow;
    }

    private void showDialogUpdateError(String error) {

    }

    @Override
    @NonNull
    public void onFailure(Call call, IOException e) {
        if (!(mContext instanceof Activity)) return;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonFunc.hideLoadingView();
                onRealFail();
            }
        });
    }

    @Override
    @NonNull
    public void onResponse(Call call, final Response response) throws IOException {
        if (!(mContext instanceof Activity)) return;
        ((Activity) mContext).runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mIsHideLoadingView)
                            CommonFunc.hideLoadingView();
                        if (!response.isSuccessful()) {
                            //CommonFunc.hideLoadingView();
                            try {
                                String msg = response.body().string();
                                JSONObject jsonResponse = new JSONObject(msg);
                                if (jsonResponse.isNull(ApiKey.MESSAGE))
                                    showDialogUpdateError(null);
                                else
                                    showDialogUpdateError(jsonResponse.getString(ApiKey.MESSAGE));
                                if(response.code() == 401){
                                    //expired token
                                    onExpriredToken();
                                }else{
                                    onRealFail();
                                }

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        try {
                            onRealSuccess(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
