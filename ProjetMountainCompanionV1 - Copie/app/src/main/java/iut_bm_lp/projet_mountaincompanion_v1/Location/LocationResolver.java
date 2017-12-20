package iut_bm_lp.projet_mountaincompanion_v1.Location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by light on 15/12/2017.
 */

public class LocationResolver implements LocationListener {

    String provider;
    private RapidGPSLock locationMgrImpl;
    private LocationManager lm;

    public LocationResolver(LocationManager lm, String provider, RapidGPSLock locationMgrImpl){

        this.lm = lm;
        this.provider = provider;
        this.locationMgrImpl = locationMgrImpl;
    }

    @Override
    public void onLocationChanged(Location location) {
        lm.removeUpdates(this);
        locationMgrImpl.locationCallback(provider);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
