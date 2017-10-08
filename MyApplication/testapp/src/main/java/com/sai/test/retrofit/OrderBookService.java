package com.sai.test.retrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sai on 2017-10-04.
 */

public interface OrderBookService {
    String BASE_URL = "https://api.coinone.co.kr/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    List<String> currencyList = new ArrayList<>(Arrays.asList(new String[] {"btc", "eth", "etc"}));

    @GET("orderbook")
    Call<OrderBook> getOrderBooks(@Query("currency") String currency);
}
