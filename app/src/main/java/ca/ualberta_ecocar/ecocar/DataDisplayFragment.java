package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DataDisplayFragment extends Fragment {

    private static final String KEY_TITLE = "TitleKey";
    private static final String KEY_STARTING_DATA_STRING = "DataKeyString";
    private static final String KEY_STARTING_DATA_FLOAT = "DataKeyFloat";
    private static final String KEY_STARTING_DATA_INT = "DataKeyInt";

    private String title;
    private float dataFloat;
    private int dataInt;
    private String dataString;
    private Object dataType;

    private View thisView;


    public DataDisplayFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_data_display, container, false);

        title = getArguments().getString(KEY_TITLE);
        if(getArguments().getString(KEY_STARTING_DATA_STRING) != null){
            dataString = getArguments().getString(KEY_STARTING_DATA_STRING);
            dataType = dataString;
        }
        else if (getArguments().getInt(KEY_STARTING_DATA_INT, -1) != -1){
            dataInt = getArguments().getInt(KEY_STARTING_DATA_INT);
            dataType = dataInt;
        }
        else{
            dataFloat = getArguments().getFloat(KEY_STARTING_DATA_FLOAT);
            dataType = dataFloat;
        }

        TextView titleView = (TextView) thisView.findViewById(R.id.TitleData);
        titleView.setText(title);

        setData(thisView);

        return thisView;
    }

    private void setData(View currentView){
        Button dataView = (Button) currentView.findViewById(R.id.CurrentData);
        if(dataString != null){
            dataView.setText(dataString);
        }
        else if (dataType instanceof Integer ){
            String outputText = dataInt + "";
            dataView.setText(outputText);
        }
        else{
            String outputText = dataFloat + "";
            dataView.setText(outputText);
        }
    }

    public void setDataFontSize(int spSize){
        try{
            Button dataView = (Button) getView().findViewById(R.id.CurrentData);
            dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }
        catch (NullPointerException e){
            Log.wtf("RaceCondition", e);
        }
    }

    public void updateData(Object data){
        Button dataView = (Button) getView().findViewById(R.id.CurrentData);
        if(data instanceof String){
            dataView.setText((String)data);
        }
        else if (data instanceof Integer ){
            String outputText = data + "";
            dataView.setText(outputText);
        }
        else{
            String outputText = data + "";
            dataView.setText(outputText);
        }
    }
}
