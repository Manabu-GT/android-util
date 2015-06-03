package com.ms_square.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Map;

public class NetUtil {

    public static final String buildQueryUrl(String baseUrl, Map<String, String> params) {
        Uri.Builder uri = new Uri.Builder();
        uri.path(baseUrl);
        for(String key : params.keySet()) {
            uri.appendQueryParameter(key, params.get(key));
        }

        return Uri.decode(uri.build().toString());
    }

    public static boolean isNetworkConnected(@NonNull Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static boolean isWifiConnected(@NonNull Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isWifiConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting() &&
                activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        return isWifiConnected;
    }

    public static boolean isWiMaxConnected(@NonNull Context context) {
        NetworkInfo activeNetwork = getActiveNetwork(context);
        boolean isWiMaxConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting() &&
                activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX;

        return isWiMaxConnected;
    }

    private static NetworkInfo getActiveNetwork(@NonNull Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
}