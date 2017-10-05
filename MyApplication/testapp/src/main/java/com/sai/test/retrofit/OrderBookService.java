package com.sai.test.retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sai on 2017-10-04.
 */

public interface OrderBookService {
    @GET("orderbook")
    Call<OrderBook> getOrderBooks(@Query("currency") String currency);
}
