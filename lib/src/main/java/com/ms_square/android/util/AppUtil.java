package com.ms_square.android.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class AppUtil {

    /**
     * Gets the application name specified by android:label in the AndroidManifest.xml.
     * It does not work if you hard-code your app name like android:label="MyApp".
     * Use a string resource such as @string/app_name.
     * @param context
     * @return application name
     */
    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    /**
     * Gets the version name in the manifest file
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        String version = "Unknown";
        if (context == null) return version;
        try {
            version =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignore) {

        } finally {
            return version;
        }
    }

    /**
     * Gets the resource id from the name and type
     * ex... getResIdFromIdentifier(ctx, "error_msg", "string");
     * @param context
     * @param name
     * @param defType
     * @return resourceId
     */
    public static int getResIdFromIdentifier(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * Checks if the target app is currently available on the device
     * @param context
     * @param targetPackageName
     * @return
     */
    public static boolean isTargetAppAvailable(Context context, String targetPackageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getApplicationInfo(targetPackageName, 0);
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
    public static void startPlayStore(Context context, String targetPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + targetPackageName)));
        } catch (ActivityNotFoundException ae) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + targetPackageName)));
        }
    }
}