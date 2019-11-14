package com.MapHelper;
import android.location.Location;
import android.os.Bundle;

import java.util.List;


/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onLocationChanged(Location location);

    void onProviderDisabled(String provider);

    void onProviderEnabled(String provider);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}