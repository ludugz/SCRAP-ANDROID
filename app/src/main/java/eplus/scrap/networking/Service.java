package eplus.scrap.networking;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nals-anhdv on 11/27/17.
 */

public class Service {
    public static void sendRegistrationToServer(Context pContext,String user_token, String device_token) {
        // TODO: Implement this method to send token to your app server.
        HashMap<String, String> hearder = new HashMap<>();
        if (!user_token.isEmpty()) {
            hearder.put("token", user_token);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("device_token", device_token);
        param.put("device_type", "A");
        DataResponse responseVeryfy = new DataResponse(pContext) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
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

        BaseRestClient.post(pContext, ApiKey.URL_NOTIFICATION_LOGIN, param, hearder, responseVeryfy, true);
    }
}
