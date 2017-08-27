package com.now.parties.flightApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by billy on 2017-08-26.
 */

public class PricesResource {
    @SerializedName("success")
    public Boolean success;
    @SerializedName("data")
    public List<Datum> data = null;

    public class Datum {
        @SerializedName("show_to_affiliates")
        public Boolean show_to_affiliates;
        @SerializedName("trip_class")
        public Integer trip_class;
        @SerializedName("origin")
        public String origin;
        @SerializedName("destination")
        public String destination;
        @SerializedName("depart_date")
        public String depart_date;
        @SerializedName("return_date")
        public String return_date;
        @SerializedName("number_of_changes")
        public Integer number_of_changes;
        @SerializedName("value")
        public Integer value;
        @SerializedName("found_at")
        public String found_at;
        @SerializedName("distance")
        public Integer distance;
        @SerializedName("actual")
        public Boolean actual;
    }


    /*
    {"success": true, "data":
        [{"show_to_affiliates":true,"trip_class":0,"origin":"BUH","destination":"TSR","depart_date":"2017-10-06","return_date":"2017-10-08","number_of_changes":0,"value":1114,"found_at":"2017-08-25T11:58:31+04:00","distance":404,"actual":true}
    */
}
