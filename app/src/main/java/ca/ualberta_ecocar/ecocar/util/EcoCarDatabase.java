package ca.ualberta_ecocar.ecocar.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URISyntaxException;

import ca.ualberta_ecocar.ecocar.R;
import io.socket.client.IO;
import io.socket.client.Socket;


public class EcoCarDatabase {

    private static String baseUrl;

    private Socket socket;
    private Gson gson;

    public EcoCarDatabase(Activity activity, boolean isObserver){
        baseUrl = activity.getString(R.string.baseURL_endpoint);
        String socketName;
        if(isObserver){
            socketName = getSocketURLObserver(activity);
        }
        else{
            socketName = getSocketURLDriver(activity);
        }
        if (socketName == null){
            // TODO: Throw error
            return;
        }
        socketName = baseUrl + socketName;
        try {
            socket = IO.socket(socketName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        gson = new Gson();
    }

    public void switchCarConnection(Activity activity, boolean isObserver){
        socket.disconnect();
        String socketName;
        if(isObserver){
            socketName = getSocketURLObserver(activity);
        }
        else{
            socketName = getSocketURLDriver(activity);
        }
        if (socketName == null){
            // TODO: Throw error
            return;
        }
        socketName = baseUrl + socketName;
        try {
            socket = IO.socket(socketName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    public void sendWebDataPacketToServer(WebDataPacket data){
        if (socket.connected()){
            socket.emit("data", gson.toJson(data));
        }
        else{
            // Try to connect again
            Log.v("EcoDatabase", "Not connected when trying to send data. Trying to connect again.");
            socket.connect();
        }
    }

    public void sendTimerUpdate(TimerDataPacket data){
        if (socket.connected()){
            socket.emit("time", gson.toJson(data));
        }
        else{
            // Try to connect again
            Log.v("EcoDatabase", "Not connected when trying to send data. Trying to connect again.");
            socket.connect();
        }
    }

    public Socket getActiveSocket(){
        return socket;
    }

    public Gson getGson(){
        return gson;
    }


    //TODO: Clean this up
    private String getSocketURLDriver(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int currentCarID = sharedPref.getInt(activity.getString(R.string.current_driving_car_save), -1);
        switch (currentCarID){
            case 0:
                return activity.getString(R.string.alice_endpoint);
            case 1:
                return activity.getString(R.string.sofie_endpoint);
            default:
                return null;
        }
    }

    private String getSocketURLObserver(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int currentCarID = sharedPref.getInt(activity.getString(R.string.current_watching_car_save), -1);
        switch (currentCarID){
            case 0:
                return activity.getString(R.string.alice_endpoint);
            case 1:
                return activity.getString(R.string.sofie_endpoint);
            default:
                return null;
        }
    }

}
