package ca.ualberta_ecocar.ecocar;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import ca.ualberta_ecocar.ecocar.util.BundleDataUtil;
import ca.ualberta_ecocar.ecocar.util.CarDataPacket;
import ca.ualberta_ecocar.ecocar.util.FcAlarmCodes;
import ca.ualberta_ecocar.ecocar.util.FcStateCodes;
import ca.ualberta_ecocar.ecocar.util.TimerDataPacket;


public class ActiveDriverInfoFragment extends Fragment {

    private DataDisplayFragment speedometerFragment;
    private DataDisplayFragment LapCounterFragment;
    private TimerDisplayFragment CurrentLapTimeFragment;
    private TimerDisplayFragment RaceTimeFragment;
    private DataDisplayFragment fcVoltageFragment;
    private DataDisplayFragment fcCurrentFragment;
    private DataDisplayFragment fcStateFragment;
    private DataDisplayFragment fcErrorFragment;

    private boolean firstTime = true;

    private int lapCount = 0;


    public ActiveDriverInfoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_active_driver_info, container, false);

        // Setup Lap Counter
        Bundle lapData = BundleDataUtil.setBaseBundleData("Lap", 0);
        LapCounterFragment = new DataDisplayFragment();
        LapCounterFragment.setArguments(lapData);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.CurrentLap, LapCounterFragment);

        // Setup Current Lap Clock
        Bundle currentLapClock = BundleDataUtil.setBaseBundleData("Current Lap", "0:00:00");
        CurrentLapTimeFragment = new TimerDisplayFragment();
        CurrentLapTimeFragment.setArguments(currentLapClock);
        transaction.replace(R.id.CurrentLapTime, CurrentLapTimeFragment);

        // Setup Race Clock
        Bundle currentRaceClock = BundleDataUtil.setBaseBundleData("Total Time", "00:00:00");
        RaceTimeFragment = new TimerDisplayFragment();
        RaceTimeFragment.setArguments(currentRaceClock);
        transaction.replace(R.id.TotalRaceTime, RaceTimeFragment);

        // Setup Fuel Cell Voltage
        Bundle fcVoltage = BundleDataUtil.setBaseBundleData("Fuel Cell Voltage", 0);
        fcVoltageFragment = new DataDisplayFragment();
        fcVoltageFragment.setArguments(fcVoltage);
        transaction.replace(R.id.FCVoltageData, fcVoltageFragment);

        // Setup Motor Current
        Bundle fcCurrent = BundleDataUtil.setBaseBundleData("Motor Current", 0);
        fcCurrentFragment = new DataDisplayFragment();
        fcCurrentFragment.setArguments(fcCurrent);
        transaction.replace(R.id.MotorCurrentData, fcCurrentFragment);

        // Setup Fuel Cell State
        Bundle fcState = BundleDataUtil.setBaseBundleData("Fuel Cell State", 0);
        fcStateFragment = new DataDisplayFragment();
        fcStateFragment.setArguments(fcState);
        transaction.replace(R.id.FCStateData, fcStateFragment);

        // Setup Fuel Cell Errors
        Bundle fcError = BundleDataUtil.setBaseBundleData("Fuel Cell Error", "FC Purge Pressure Low");
        fcErrorFragment = new DataDisplayFragment();
        fcErrorFragment.setArguments(fcError);
        transaction.replace(R.id.FCErrorData, fcErrorFragment);

        // Setup Speedometer
        Bundle speedometer = BundleDataUtil.setBaseBundleData("Speed", 23.0f);
        speedometerFragment = new DataDisplayFragment();
        speedometerFragment.setArguments(speedometer);
        transaction.replace(R.id.Speedometer, speedometerFragment);

        transaction.commit();

        return currentView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void updateBluetoothOnScreenData(CarDataPacket data){
        if(firstTime){
            speedometerFragment.setDataFontSize(160);
            fcStateFragment.setDataFontSize(10);
            fcErrorFragment.setDataFontSize(10);
            firstTime = false;
        }
        DecimalFormat speedFormat = new DecimalFormat("###.0");
        speedometerFragment.updateData(speedFormat.format(data.getSpeed()));
        fcVoltageFragment.updateData(data.getFCVoltage());
        fcCurrentFragment.updateData(data.getMotor_current());
        fcStateFragment.updateData(FcStateCodes.getFCStateFromUnsignedInt(data.getFc_state()));
        fcErrorFragment.updateData(FcAlarmCodes.getFCErrorFromUnsignedInt(data.getFc_alarm_code()));
    }

    public void updateTimeData(TimerDataPacket data){
        if(lapCount != data.getCurrentLap()){
            lapCount = data.getCurrentLap();
            LapCounterFragment.updateData(data.getCurrentLap());
            CurrentLapTimeFragment.resetTimer();
        }

        if(data.getStartRaceTime() == 0){
            CurrentLapTimeFragment.resetTimer();
            RaceTimeFragment.resetTimer();
        }

        if(data.isActive()){
            if(!CurrentLapTimeFragment.isTimerActiver()){
                CurrentLapTimeFragment.setStartTime(data.getStartLapTime());
                CurrentLapTimeFragment.startTimer();
            }
            if(!RaceTimeFragment.isTimerActiver()){
                RaceTimeFragment.setStartTime(data.getStartRaceTime());
                RaceTimeFragment.startTimer();
            }
        }
        else{
            if(CurrentLapTimeFragment.isTimerActiver()){
                CurrentLapTimeFragment.stopTimer();
            }
            if(RaceTimeFragment.isTimerActiver()){
                RaceTimeFragment.stopTimer();
            }
        }

    }




}
