package eplus.scrap;

/**
 * Created by nals-anhdv on 10/24/17.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.ms_square.etsyblur.AsyncPolicy;
import com.ms_square.etsyblur.SmartAsyncPolicy;

public enum SmartAsyncPolicyHolder {
    INSTANCE;

    private AsyncPolicy smartAsyncPolicy;

    public void init(@NonNull Context context) {
        smartAsyncPolicy = new SmartAsyncPolicy(context, true);
    }

    public AsyncPolicy smartAsyncPolicy() {
        return smartAsyncPolicy;
    }
}