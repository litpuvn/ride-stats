package edu.ttu.spm.cheapride;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.json.JSONObject;

import java.util.HashMap;

public abstract class AbstractNetworkRequest {

    protected static final int READ_TIMEOUT = 30000; // seconds
    protected static final int CONNECTION_TIMEOUT = 30000; // seconds

    protected JSONObject response;
    protected Context mContext;

    public abstract String performPostCall(String requestURL,
                                  HashMap<Object, Object> postDataParams);


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public abstract void showProgress(final boolean show);
}
