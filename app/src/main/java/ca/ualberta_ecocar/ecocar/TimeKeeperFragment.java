package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import ca.ualberta_ecocar.ecocar.util.BundleDataUtil;
import ca.ualberta_ecocar.ecocar.util.EcoCarDatabase;
import ca.ualberta_ecocar.ecocar.util.TimerDataPacket;
import ca.ualberta_ecocar.ecocar.util.WebDataPacket;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TimeKeeperFragment extends Fragment {

    boolean isButtonStart = true;
    int LapCount = 0;

    DataDisplayFragment LapCounterFragment;
    TimerDisplayFragment CurrentLapTimeFragment;
    TimerDisplayFragment RaceTimeFragment;

    DataDisplayFragment SpeedFragment;
    DataDisplayFragment EfficiencyFragment;
    DataDisplayFragment FCAlarmFragment;
    DataDisplayFragment FCStateFragment;
    DataDisplayFragment FCVoltageFragment;
    DataDisplayFragment MotorCurrentFragment;

    EcoCarDatabase database;
    Socket socket;

    public TimeKeeperFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_time_keeper, container, false);

        database = new EcoCarDatabase(getActivity(), true);
        socket = database.getActiveSocket();

        // Setup Lap Counter
        final Bundle lapData = BundleDataUtil.setBaseBundleData("Lap", 0);
        LapCounterFragment = new DataDisplayFragment();
        LapCounterFragment.setArguments(lapData);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.TimeKeeperCurrentLap, LapCounterFragment);

        // Setup Current Lap Clock
        Bundle currentLapClock = BundleDataUtil.setBaseBundleData("Current Lap", "0:00:00");
        CurrentLapTimeFragment = new TimerDisplayFragment();
        CurrentLapTimeFragment.setArguments(currentLapClock);
        transaction.replace(R.id.TimeKeeperCurrentLapTime, CurrentLapTimeFragment);

        // Setup Race Clock
        Bundle currentRaceClock = BundleDataUtil.setBaseBundleData("Race Time", "0:00:00");
        RaceTimeFragment = new TimerDisplayFragment();
        RaceTimeFragment.setArguments(currentRaceClock);
        transaction.replace(R.id.TimeKeeperRaceTime, RaceTimeFragment);

        // Setup Time Keeper Speed
        Bundle currentSpeed = BundleDataUtil.setBaseBundleData("Speed", 0);
        SpeedFragment = new DataDisplayFragment();
        SpeedFragment.setArguments(currentSpeed);
        transaction.replace(R.id.TimeKeeperSpeed, SpeedFragment);

        // Setup Time Keeper Efficiency
        Bundle currentEff = BundleDataUtil.setBaseBundleData("Efficiency", 0);
        EfficiencyFragment = new DataDisplayFragment();
        EfficiencyFragment.setArguments(currentEff);
        transaction.replace(R.id.TimeKeeperEfficiency, EfficiencyFragment);

        // Setup Time FC Alarm Fragment
        Bundle FCAlarm = BundleDataUtil.setBaseBundleData("Fuel Cell Error", "No New Data");
        FCAlarmFragment = new DataDisplayFragment();
        FCAlarmFragment.setArguments(FCAlarm);
        transaction.replace(R.id.TimeKeeperFCAlarm, FCAlarmFragment);

        // Setup Time FC State Fragment
        Bundle FCState = BundleDataUtil.setBaseBundleData("Fuel Cell State", "No new data");
        FCStateFragment = new DataDisplayFragment();
        FCStateFragment.setArguments(FCState);
        transaction.replace(R.id.TimeKeeperFCState, FCStateFragment);


        // Setup Time FC Voltage Fragment
        Bundle FCVoltage = BundleDataUtil.setBaseBundleData("Fuel Cell Voltage", 0);
        FCVoltageFragment = new DataDisplayFragment();
        FCVoltageFragment.setArguments(FCVoltage);
        transaction.replace(R.id.TimeKeeperFCVoltage, FCVoltageFragment);

        // Setup Time Motor Current Fragment
        Bundle MotorCurrent = BundleDataUtil.setBaseBundleData("Motor Current", 0);
        MotorCurrentFragment = new DataDisplayFragment();
        MotorCurrentFragment.setArguments(MotorCurrent);
        transaction.replace(R.id.TimeKeeperMotorCurrent, MotorCurrentFragment);

        transaction.commit();

        final Button StartRaceButton = (Button) currentView.findViewById(R.id.StartRace);
        StartRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButtonStart)
                {
                    CurrentLapTimeFragment.startTimer();
                    RaceTimeFragment.startTimer();
                    StartRaceButton.setText("Pause");
                    isButtonStart = false;
                    TimerDataPacket dataPacket = new TimerDataPacket(RaceTimeFragment.getTotalMilliseconds(),
                            CurrentLapTimeFragment.getTotalMilliseconds(), LapCount, true);
                    database.sendTimerUpdate(dataPacket);
                }
                else
                {
                    CurrentLapTimeFragment.stopTimer();
                    RaceTimeFragment.stopTimer();
                    StartRaceButton.setText("Start");
                    isButtonStart = true;
                    TimerDataPacket dataPacket = new TimerDataPacket(RaceTimeFragment.getTotalMilliseconds(),
                            CurrentLapTimeFragment.getTotalMilliseconds(), LapCount, false);
                    database.sendTimerUpdate(dataPacket);
                }
            }
        });

        Button ResetRaceButton = (Button) currentView.findViewById(R.id.ResetStopRace);
        ResetRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentLapTimeFragment.resetTimer();
                RaceTimeFragment.resetTimer();
                LapCount = 0;
                StartRaceButton.setText("Start");
                isButtonStart = true;
                updateLapCounterFragment();
                TimerDataPacket dataPacket = new TimerDataPacket(RaceTimeFragment.getTotalMilliseconds(),
                        CurrentLapTimeFragment.getTotalMilliseconds(), LapCount, false);
                database.sendTimerUpdate(dataPacket);
            }
        });
        Button IncreaseLapButton = (Button) currentView.findViewById(R.id.IncreaseLap);
        IncreaseLapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LapCount += 1;
                if(RaceTimeFragment.isTimerActiver()){
                    CurrentLapTimeFragment.resetTimer();
                    CurrentLapTimeFragment.startTimer();
                }
                updateLapCounterFragment();
                TimerDataPacket dataPacket = new TimerDataPacket(RaceTimeFragment.getTotalMilliseconds(),
                        CurrentLapTimeFragment.getTotalMilliseconds(), LapCount, RaceTimeFragment.isTimerActiver());
                database.sendTimerUpdate(dataPacket);


            }
        });
        Button DecreaseLapButton = (Button) currentView.findViewById(R.id.DecreaseLap);
        DecreaseLapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LapCount <= 0){
                    return;
                }
                LapCount -= 1;
                if(RaceTimeFragment.isTimerActiver()){
                    CurrentLapTimeFragment.resetTimer();
                    CurrentLapTimeFragment.startTimer();
                }
                updateLapCounterFragment();
                TimerDataPacket dataPacket = new TimerDataPacket(RaceTimeFragment.getTotalMilliseconds(),
                        CurrentLapTimeFragment.getTotalMilliseconds(), LapCount, RaceTimeFragment.isTimerActiver());
                database.sendTimerUpdate(dataPacket);

            }
        });

        setupServerDataSocket();

        return currentView;
    }

    public void updateLapCounterFragment(){
        LapCounterFragment.updateData(LapCount);
    }

    public void setupServerDataSocket(){
        socket.on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                JSONObject obj = (JSONObject)args[0];
                WebDataPacket newData = gson.fromJson(obj.toString(), WebDataPacket.class);
                if(newData != null){
                    updateDataFragments(newData);
                }
                else{
                    Log.wtf("JSON error", "setupServerDataSocket JSON Error" + args[0]);
                }
            }
        });
    }

    public void updateDataFragments(WebDataPacket dataPacket){
        SpeedFragment.updateData(dataPacket.getSpeed());
        EfficiencyFragment.updateData(dataPacket.getEfficiency());
        FCAlarmFragment.updateData(dataPacket.getFc_alarm_code());
        FCStateFragment.updateData(dataPacket.getFc_state());
        FCVoltageFragment.updateData(dataPacket.getFCVoltage());
        MotorCurrentFragment.updateData(dataPacket.getMotor_current());
    }

    @Override
    public void onStart(){
        super.onStart();
        LapCounterFragment.setDataFontSize(40);
        CurrentLapTimeFragment.setDataFontSize(40);
        RaceTimeFragment.setDataFontSize(40);

        SpeedFragment.setDataFontSize(40);
        EfficiencyFragment.setDataFontSize(40);
        FCAlarmFragment.setDataFontSize(20);
        FCStateFragment.setDataFontSize(20);
        FCVoltageFragment.setDataFontSize(40);
        MotorCurrentFragment.setDataFontSize(40);

    }


}
