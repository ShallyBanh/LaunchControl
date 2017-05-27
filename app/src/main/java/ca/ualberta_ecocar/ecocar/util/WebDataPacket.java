package ca.ualberta_ecocar.ecocar.util;

public class WebDataPacket {


    private double carLatitude;
    private double carLongitude;
    private float speed;
    private float fc_voltage;
    private float motor_current;
    private float efficiency;
    private String fc_alarm_code;
    private String fc_state;
    private float fc_temp;


    WebDataPacket(){}

    public WebDataPacket(CarDataPacket carData, double newCarLatitude, double newCarLongitude){
        speed = carData.getSpeed();
        fc_voltage = carData.getFCVoltage();
        motor_current = carData.getMotor_current();
        fc_alarm_code = FcAlarmCodes.getFCErrorFromUnsignedInt(carData.getFc_alarm_code());
        fc_state = FcStateCodes.getFCStateFromUnsignedInt(carData.getFc_state());
        fc_temp = carData.getFc_temp();
        efficiency = carData.getEfficiency();
        carLatitude = newCarLatitude;
        carLongitude = newCarLongitude;
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

    public String getFc_alarm_code() {
        return fc_alarm_code;
    }

    public String getFc_state() {
        return fc_state;
    }

    public float getFc_temp() {
        return fc_temp;
    }

    public double getCarLatitude() {
        return carLatitude;
    }

    public double getCarLongitude() {
        return carLongitude;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
