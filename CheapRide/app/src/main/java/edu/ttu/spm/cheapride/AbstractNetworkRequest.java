package edu.ttu.spm.cheapride;


import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONObject;

import java.util.HashMap;

public abstract class AbstractNetworkRequest {

    protected JSONObject response;

    public abstract String performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams);


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public abstract void showProgress(final boolean show);
}
