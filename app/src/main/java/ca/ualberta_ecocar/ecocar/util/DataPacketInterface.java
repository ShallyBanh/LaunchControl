package ca.ualberta_ecocar.ecocar.util;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DataPacketInterface {
    String baseUrl = "Put baseUrl here";

    @POST(baseUrl)
    Call<WebDataPacket> shoveCarDataToCloud(@Body WebDataPacket data);

    @POST(baseUrl)
    Call<TimerDataPacket> shoveTimeDataToCloud(@Body TimerDataPacket data);

}

