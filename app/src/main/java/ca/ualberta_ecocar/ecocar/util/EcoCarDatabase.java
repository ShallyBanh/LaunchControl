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
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EcoCarDatabase {

    private static String baseUrl;

    private Socket socket;
    private Gson gson;
    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private DataPacketInterface client;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

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

        builder =
                new Retrofit.Builder()
                        .baseUrl(socketName)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        client =  retrofit.create(DataPacketInterface.class);
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

        builder =
                new Retrofit.Builder()
                        .baseUrl(socketName)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        client =  retrofit.create(DataPacketInterface.class);
    }

    public void sendWebDataPacketToServer(WebDataPacket data){
        client.shoveCarDataToCloud(data);
        //socket.emit("data", gson.toJson(data));

    }

    public void sendTimerUpdate(TimerDataPacket data){
        client.shoveTimeDataToCloud(data);
        //socket.emit("time", gson.toJson(data));
    }

    public Socket getActiveSocket(){
        return socket;
    }

    public DataPacketInterface getRetrofitClient(){
        return client;
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
