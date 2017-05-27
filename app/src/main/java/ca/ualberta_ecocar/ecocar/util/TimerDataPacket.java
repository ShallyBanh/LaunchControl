package ca.ualberta_ecocar.ecocar.util;


public class TimerDataPacket {
    private long startRaceTime;
    private long startLapTime;
    private int currentLap;
    private boolean isActive;

    TimerDataPacket() {}

    public TimerDataPacket(long startRaceTime, long startLapTime, int currentLap, boolean isActive) {
        this.startRaceTime = startRaceTime;
        this.startLapTime = startLapTime;
        this.currentLap = currentLap;
        this.isActive = isActive;
    }


    public long getStartRaceTime() {
        return startRaceTime;
    }

    public long getStartLapTime() {
        return startLapTime;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public boolean isActive() {
        return isActive;
    }
}
