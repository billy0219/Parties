package com.now.parties.flightApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by billy on 2017-08-26.
 */

public class BaseService {
    protected static Object retrofit(Class<?> className)
    {
        String host = "http://api.travelpayouts.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(host).addConverterFactory(GsonConverterFactory.create()).build();
        //    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        //    return new Retrofit.Builder().baseUrl(host).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //    return new Retrofit.Builder().baseUrl(host).addConverterFactory(GsonConverterFactory.create()).client(client()).build();
        //    return new Retrofit.Builder().baseUrl(host).addConverterFactory(GsonConverterFactory.create(gson)).client(client()).build();
        return retrofit.create(className);
    }
}
