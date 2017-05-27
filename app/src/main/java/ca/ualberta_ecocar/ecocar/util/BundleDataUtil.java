package ca.ualberta_ecocar.ecocar.util;


import android.os.Bundle;

public class BundleDataUtil {

    private static final String KEY_TITLE = "TitleKey";
    private static final String KEY_STARTING_DATA_STRING = "DataKeyString";
    private static final String KEY_STARTING_DATA_FLOAT = "DataKeyFloat";
    private static final String KEY_STARTING_DATA_INT = "DataKeyInt";

    public static Bundle setBaseBundleData(String title, float data){
        Bundle dataToSend = new Bundle();
        dataToSend.putString(KEY_TITLE, title);
        dataToSend.putFloat(KEY_STARTING_DATA_FLOAT, data);
        return dataToSend;
    }

    public static Bundle setBaseBundleData(String title, String data){
        Bundle dataToSend = new Bundle();
        dataToSend.putString(KEY_TITLE, title);
        dataToSend.putString(KEY_STARTING_DATA_STRING, data);
        return dataToSend;
    }

    public static Bundle setBaseBundleData(String title, int data){
        Bundle dataToSend = new Bundle();
        dataToSend.putString(KEY_TITLE, title);
        dataToSend.putInt(KEY_STARTING_DATA_INT, data);
        return dataToSend;
    }
}
