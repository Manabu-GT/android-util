package com.ms_square.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.Map;

public class NetUtil {

    public static final String buildUrl(String baseUrl, Map<String, String> params) {
        Uri.Builder uri = new Uri.Builder();
        uri.path(baseUrl);
        for(String key : params.keySet()) {
            uri.appendQueryParameter(key, params.get(key));
        }
        return Uri.decode(uri.build().toString());
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isWifiConnected = activeNetwork != null && activeNetwork.isConnected() &&
                activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        return isWifiConnected;
    }

    public static boolean isWifiOrWiMaxConnected(Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isWifiWiMaxConnected = activeNetwork != null && activeNetwork.isConnected() &&
                (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX);

        return isWifiWiMaxConnected;
    }

    private static NetworkInfo getActiveNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }
}