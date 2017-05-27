package ca.ualberta_ecocar.ecocar.util;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;

public class EcoCarTimer {

    private long MillisecondTime, StartTime, TimeBuff, UpdateTime, TotalMillisecondsPassed= 0L ;
    private Handler handler;
    private int Seconds, Minutes, MilliSeconds;
    private Button text;
    private boolean isActive;

    public EcoCarTimer(Button textToUpdate){
        text = textToUpdate;
        handler = new Handler();
        isActive = false;
    }


    public void start(){
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        isActive = true;
    }

    public void pause(){
        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
        isActive = false;
    }

    public long getTotalMillisecondsPassed(){
        return TotalMillisecondsPassed;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setTimeBuff(long newBuff){
        TimeBuff = newBuff;
    }

    public void reset(){
        pause();
        TotalMillisecondsPassed = 0L;
        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

        text.setText("00:00:00");
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            TotalMillisecondsPassed = UpdateTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            text.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };
}
