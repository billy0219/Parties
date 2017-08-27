package com.now.parties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.now.parties.flightApi.PricesResource;
import com.now.parties.flightApi.TravelPayoutService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallMainActivity extends AppCompatActivity {

    private TextView mApiCallTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_call_main);

        mApiCallTextView = (TextView) findViewById(R.id.apiCallTextView);

        /**
         GET List Resources
         **/
        Call call = TravelPayoutService.api().prices("rub", "year", 1, 30, true, "price", 0);
        call.enqueue(new Callback<PricesResource>() {
            @Override
            public void onResponse(Call<PricesResource> call, Response<PricesResource> response) {
                Log.d("TAG", response.code() + "");
                mApiCallTextView.append("onResponse:"+response.code());

                if (response != null && response.isSuccessful() && response.body() != null)
                {
                    PricesResource resource = response.body();
                    Boolean success = resource.success;
                    List<PricesResource.Datum> datumList = resource.data;

                    for (PricesResource.Datum datum : datumList) {
                        String displayResponse = "origin:" + datum.origin
                                +", destination:" + datum.destination
                                +", depart_date:" + datum.depart_date
                                +", return_date:" + datum.return_date;
                        mApiCallTextView.append("\n" + displayResponse);
                    }

                    //Log.i("MainActivity | prices", type.name + ", " + type.url);
                }
            }

            @Override
            public void onFailure(Call<PricesResource> call, Throwable t) {
                call.cancel();
                mApiCallTextView.append("onFailure");
            }
        });
    }
}
