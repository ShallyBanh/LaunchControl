package ca.ualberta_ecocar.ecocar.util;


public enum FcStateCodes {

    STANDBY(0, "Standby"),
    SHUTDOWN(1, "Shutdown"),
    STARTUP_FANS(2, "Startup Fans"),
    STARTUP_H2(3, "Startup H2"),
    STARTUP_PURGE(4, "Startup Purge"),
    STARTUP_CHARGE(5, "Startup Charge"),
    RUN(6, "Run"),
    SevenPlaceHolder(7, "If you are reading this message Nathan"),
    ALARM(8, "Alarm"),
    REPRESSURIZE(9, "Repressurize"),
    MANUAL_DEPRESSURIZE(10, "Manual Depressurize"),
    AIR_STARVE(11, "Air Starve"),
    NO_CONNECTION(-1, "No connection");

    private int id;
    private String message;

    FcStateCodes(int inputId, String readableMessage){
        this.id = inputId;
        message = readableMessage;
    }

    public static FcStateCodes getFCStateFromID(int idValue) {
        for(FcStateCodes v : values())
            if(v.getId() == idValue) return v;
        return NO_CONNECTION;
    }

    public static String getFCStateFromUnsignedInt(int binaryErrorNumber){
        String outputState = "";
        boolean[] bits = new boolean[12];
        for (int i = 11; i >= 0; i--){
            bits[i] = (binaryErrorNumber & (1 << i)) != 0;
        }

        for(int i = 0; i <= 11; i++){
            if(bits[i]){
                if(outputState.equals("")){
                    outputState += getFCStateFromID(i).message;
                }
                else{
                    outputState += ", " + getFCStateFromID(i).message;
                }
            }
        }
        if(outputState.equals("")){
            return NO_CONNECTION.message;
        }
        return outputState;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
