package com.now.parties.flightApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by billy on 2017-08-26.
 */

public class TravelPayoutService extends BaseService {
    public static FlightAPI api()
    {
        return (FlightAPI) retrofit(FlightAPI.class);
    }

    public interface FlightAPI
    {
        @Headers({
                "X-Access-Token: f25ae4959c6152d52f9e516b6aafc7e0"
        })
        @GET("v2/prices/latest?")
        Call<PricesResource> prices(@Query("currency") String currency,
                                    @Query("period_type") String period_type,
                                    @Query("page") Integer page,
                                    @Query("limit") Integer limit,
                                    @Query("show_to_affiliates") Boolean show_to_affiliates,
                                    @Query("sorting") String sorting,
                                    @Query("trip_class") Integer trip_class);
        /*
        "http://api.travelpayouts.com/v2/prices/latest?currency=rub&period_type=year&page=1&limit=30&show_to_affiliates=true&sorting=price&trip_class=0"
        */

        /*
        @GET("{name}.json")
        Call<Json1Item[]> json2(@Path("name") String name);

        @GET("{name}.json")
        Call<Json3Item> json3(@Path("name") String name);

        @GET("{name}.json")
        Call<Json4Item> json4(@Path("name") String name);
        */
    }

}
