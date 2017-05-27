package ca.ualberta_ecocar.ecocar.util;

public class CarDataPacket {

    // speed, fuel cell voltage, cap voltage, fc current, cap current, fc temp
    private float speed;
    private float efficiency;
    private float fc_voltage;
    private float motor_current;
    private int fc_alarm_code;
    private int fc_state;
    private float fc_temp;

    CarDataPacket(){
    }

    public float getSpeed(){
        return speed;
    }

    public float getFCVoltage() {
        return fc_voltage;
    }

    public float getMotor_current() {
        return motor_current;
    }

    public int getFc_alarm_code() {
        return fc_alarm_code;
    }

    public int getFc_state() {
        return fc_state;
    }

    public float getFc_temp() {
        return fc_temp;
    }

    public float getEfficiency() {return efficiency;}

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
