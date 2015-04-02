package com.ms_square.android.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     * Gets the application name specified by android:label in the AndroidManifest.xml.
     * It does not work if you hard-code your app name like android:label="MyApp".
     * Use a string resource such as @string/app_name.
     * @param context
     * @return application name
     */
    public static String getApplicationName(@NonNull Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    /**
     * Gets the version name in the manifest file
     * @param context
     * @return
     */
    @NonNull
    public static String getVersion(@Nullable Context context) {
        String version = "Unknown";
        if (context == null) return version;
        try {
            version =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            Log.w(TAG, "Package Not found:" + context.getPackageName());
        }
        return version;
    }

    /**
     * Gets the resource id from the name and type
     * ex... getResIdFromIdentifier(ctx, "error_msg", "string");
     * @param context
     * @param name
     * @param defType
     * @return resourceId
     */
    public static int getResIdFromIdentifier(@NonNull Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * Checks if the target app is installed on the device
     * @param context
     * @param targetPackageName
     * @return
     */
    public static boolean isAppInstalled(@NonNull Context context, String targetPackageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getApplicationInfo(targetPackageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ne) {
            return false;
        }
    }

    /**
     * Opens a native GooglePlay app if available; otherwise, open GooglePlay site via a web browser.
     * @param context
     * @param targetPackageName - package name of the target app the GooglePlay opens
     */
    public static void startPlayStore(@NonNull Context context, String targetPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + targetPackageName)));
        } catch (ActivityNotFoundException ae) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + targetPackageName)));
        }
    }
}