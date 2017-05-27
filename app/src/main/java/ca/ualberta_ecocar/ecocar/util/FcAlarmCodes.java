package ca.ualberta_ecocar.ecocar.util;

public enum FcAlarmCodes {

    SUPER_CAPACITORS_DISCONNECTED(0, "Super Capacitors Disconnected"),
    FUEL_CELL_DISCONNECTED(1, "Fuel Cell Disconnected"),
    RESISTORS_DISCONNECTED(2, "Resistors Disconnected"),
    H2OK_SIGNAL_LOW(3, "H2OK Signal Low"),
    FC_TEMPERATURE_LOW (4, "FC Temperature Low"),
    FC_TEMPERATURE_HIGH (5, "FC Temperature High"),
    FC_PRESSURE_HIGH(6, "FC Pressure High"),
    FC_PRESSURE_LOW(7, "FC Pressure Low"),
    FC_OVER_CURRENT(8, "FC Over Current"),
    FC_UNDER_CURRENT(9, "FC Under Current"),
    FC_OVER_VOLT(10, "FC Over Volt"),
    FC_UNDER_VOLT(11, "FC Under Volt"),
    WATCH_DOG_TIMER(12, "Watch Dog Timer"),
    BROWN_OUT(13, "Brown Out"),
    FC_POWER_BAD(14, "FC Power Bad"),
    FC_PURGE_PRESSURE_LOW(15, "FC Purge Pressure Low"),
    NO_ERROR(-1, "No error");


    private int id;
    private String message;

    FcAlarmCodes(int inputId, String readableMessage){
        this.id = inputId;
        message = readableMessage;
    }

    public static FcAlarmCodes getFCErrorFromID(int idValue) {
        for(FcAlarmCodes v : values())
            if(v.getId() == idValue) return v;
        return NO_ERROR;
    }

    public static String getFCErrorFromUnsignedInt(int binaryErrorNumber){
        String outputError = "";
        boolean[] bits = new boolean[16];
        for (int i = 15; i >= 0; i--){
            bits[i] = (binaryErrorNumber & (1 << i)) != 0;
        }

        for(int i = 0; i <= 15; i++){
            if(bits[i]){
                if(outputError.equals("")){
                    outputError += getFCErrorFromID(i).message;
                }
                else{
                    outputError += ", " + getFCErrorFromID(i).message;
                }
            }
        }
        if(outputError.equals("")){
            return NO_ERROR.message;
        }
        return outputError;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
