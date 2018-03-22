package eplus.scrap;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import eplus.scrap.common.SharePreferences;
import eplus.scrap.networking.ApiKey;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nals-anhdv on 11/27/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    //private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        String log_token = SharePreferences.getStringPreference(getApplicationContext(), "token");
        HashMap<String, String> hearder = new HashMap<>();
        if (!token.isEmpty()) {
            hearder.put("token", log_token);
        }
            DataResponse responseVeryfy = new DataResponse(this) {
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
            HashMap<String, String> param = new HashMap<>();
            param.put("device_token", token);
            param.put("device_type", "A");
            BaseRestClient.post(this, ApiKey.URL_NOTIFICATION_LOGIN, param, hearder, responseVeryfy, true);
        }
}
