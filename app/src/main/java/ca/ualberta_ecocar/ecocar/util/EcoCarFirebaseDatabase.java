package ca.ualberta_ecocar.ecocar.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.ualberta_ecocar.ecocar.R;

/**
 * Created by Nathan on 2017-03-07.
 */

public class EcoCarFirebaseDatabase {

    private static FirebaseDatabase mDatabase;

    EcoCarFirebaseDatabase(){
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void sendWebDataPacketToServer(Activity activity, WebDataPacket data){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int currentCarID = sharedPref.getInt(activity.getString(R.string.current_driving_car_save), -1);
        DatabaseReference reference = mDatabase.getReference();
        String carName;
        switch (currentCarID){
            case 0:
                carName = "Alice";
                break;
            case 1:
                carName = "Prototype";
                break;
            default:
                // Log error
                return;
        }
        if (carName != null){
            //TODO: Figure out server side time
            long serverTime = System.currentTimeMillis();
            reference.child(carName).child(serverTime + "").setValue(data);
        }
    }
}
