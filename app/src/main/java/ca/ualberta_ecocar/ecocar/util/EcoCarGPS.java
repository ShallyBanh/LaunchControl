package ca.ualberta_ecocar.ecocar.util;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

// Used https://www.tutorialspoint.com/android/android_location_based_services.htm as a reference
public class EcoCarGPS extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATES_TIME = 1000 * 10; // 10 seconds

    protected LocationManager locationManager;

    private Location currentLocation;

    private float CurrentGPSSpeed;

    public EcoCarGPS(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Log perms error
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_UPDATES_TIME,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (locationManager != null) {
            currentLocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public Location getLocation(){
        return currentLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // in ms
        long timePassedSinceLastLocationChanged = location.getTime() - currentLocation.getTime();
        // In m
        float distanceBetween = location.distanceTo(location);

        //TODO: Check units
        CurrentGPSSpeed = (distanceBetween / 1000.0f)/(timePassedSinceLastLocationChanged/1000.0f);

        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public float getCurrentGPSSpeed() {
        return CurrentGPSSpeed;
    }
}
