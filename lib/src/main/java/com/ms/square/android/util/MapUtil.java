package com.ms.square.android.util;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;

public class MapUtil {

    // earth's radius (mean radius = 6,371km)
    private static final int EARTH_RADIUS = 6371;

    // based on http://en.wikipedia.org/wiki/Haversine_formula
    public static double calcDistance(Location loc1, Location loc2) {
        double lat1 = loc1.getLatitude();
        double lng1 = loc1.getLongitude();
        double lat2 = loc2.getLatitude();
        double lng2 = loc2.getLongitude();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    // in meters per second
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static double calcSpeed(Location loc1, Location loc2) {
        // in km
        double distance = calcDistance(loc1, loc2);
        // in second
        double timeDiff;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            timeDiff = (loc2.getElapsedRealtimeNanos() - loc1.getElapsedRealtimeNanos()) / 1000000000;
        } else {
            // could be inaccurate because the UTC time on a device is not monotonic.
            timeDiff = (loc2.getTime() - loc1.getTime()) / 1000;
        }
        return distance * 1000 / timeDiff;
    }
}