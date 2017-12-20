package iut_bm_lp.projet_mountaincompanion_v1.Location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by light on 14/12/2017.
 */

public class LocationObserver implements LocationListener {

    private RapidGPSLock myController;

    public LocationObserver(RapidGPSLock myController){

        super();
        this.myController = myController;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Log.d("MountainCompanion", "new location(ob) " + location.getAccuracy() + " " + location.getLatitude() + "," + location.getLongitude());
            myController.setPosition(location);
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
