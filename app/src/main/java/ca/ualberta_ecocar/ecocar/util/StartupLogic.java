package ca.ualberta_ecocar.ecocar.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ca.ualberta_ecocar.ecocar.BluetoothSetupFragment;
import ca.ualberta_ecocar.ecocar.ChooseCarFragment;
import ca.ualberta_ecocar.ecocar.CollectPermissionsFragment;
import ca.ualberta_ecocar.ecocar.R;

/**
 * Created by Nathan on 2017-02-23.
 */

public class StartupLogic {

    public static Fragment getSetupFragment(Activity activity) {
        if (PermsUtil.checkPermission(activity)) {
            // Get all the perms
            return new CollectPermissionsFragment();
        }
        // You have perms yay! Move on to selecting a car!

                    /*
                    * -1 = Not set
                    * 0 = Alice
                    * 1 = Prototype
                    * */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int currentCarID = sharedPref.getInt(activity.getString(R.string.current_driving_car_save), -1);
        String btDevice = sharedPref.getString(activity.getString(R.string.current_bluetooth_device_address_save), "failed");
        if (currentCarID == -1) {
            // Open up car selection menu
            return new ChooseCarFragment();
        } else if (btDevice.equals("failed")) {
            // Open bluetooth
            return new BluetoothSetupFragment();
        } else {
            // Move to drivers screen
            return null;
        }
    }
}
