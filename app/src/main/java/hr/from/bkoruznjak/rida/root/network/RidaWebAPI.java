package hr.from.bkoruznjak.rida.root.network;

import com.google.gson.JsonObject;

import hr.from.bkoruznjak.rida.current.model.RidePoint;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface RidaWebAPI {

    String BASE_URL = "http://test.mother.i-ways.hr";

    @POST("./")
    Call<JsonObject> sendRideInfo(
            @Query("json") int json,
            @Body RidePoint rideInfoObject);
}
