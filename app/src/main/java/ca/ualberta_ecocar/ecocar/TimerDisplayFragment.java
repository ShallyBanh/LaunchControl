package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import ca.ualberta_ecocar.ecocar.util.EcoCarTimer;

public class TimerDisplayFragment extends Fragment {

    private static final String KEY_TITLE = "TitleKey";
    private static final String KEY_STARTING_DATA_TIME = "DataKeyString";

    private String title;
    private String dataString;

    private View thisView;
    private EcoCarTimer localTimer;
    private Button dataView;


    public TimerDisplayFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_data_display, container, false);

        title = getArguments().getString(KEY_TITLE);
        if(getArguments().getString(KEY_STARTING_DATA_TIME) != null){
            dataString = getArguments().getString(KEY_STARTING_DATA_TIME);
        }

        TextView titleView = (TextView) thisView.findViewById(R.id.TitleData);
        titleView.setText(title);
        dataView = (Button) thisView.findViewById(R.id.CurrentData);
        dataView.setText(dataString);
        localTimer = new EcoCarTimer(dataView);

        return thisView;
    }


    public void setDataFontSize(int spSize){
        try{
            dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }
        catch (NullPointerException e){
            Log.wtf("RaceCondition", e);
        }
    }

    public void startTimer(){
        localTimer.start();
    }

    public long getTotalMilliseconds(){
        return localTimer.getTotalMillisecondsPassed();
    }

    public boolean isTimerActiver(){
        return localTimer.isActive();
    }

    public void setStartTime(long startTime){
        localTimer.setTimeBuff(startTime);
    }

    public void stopTimer(){
        localTimer.pause();
    }

    public void resetTimer(){
        localTimer.reset();
    }

}
