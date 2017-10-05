package com.sai.test.testapp;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.sai.test.RestfulRecyclerView.OrderBookAdapter;
import com.sai.test.retrofit.OrderBook;
import com.sai.test.retrofit.OrderBookService;
import com.sai.test.testapp.databinding.ActivityRetrofitBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 100;
    private static final String RECEIVER_PERMISSION = "android.permission.INTERNET";

    ActivityRetrofitBinding mBinding;
    OrderBookAdapter mOrderBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retrofit);
        mBinding.content.setVariable(BR.retrofitActivity, this);
        setSupportActionBar(mBinding.toolbar);
        mBinding.content.rvOutput.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission(RECEIVER_PERMISSION);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.button_run) {
            queryOrderBookData("https://api.coinone.co.kr/", "eth");
        }
    }

    private void queryOrderBookData(String url, String currency) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderBookService orderBookService = retrofit.create(OrderBookService.class);
        Call<OrderBook> call = orderBookService.getOrderBooks(currency);
        call.enqueue(new Callback<OrderBook>() {
            @Override
            public void onResponse(Call<OrderBook> call, Response<OrderBook> response) {
                mOrderBookAdapter = new OrderBookAdapter<>(R.layout.orderbook_item, BR.order, response.body().bid);
                mBinding.content.rvOutput.setAdapter(mOrderBookAdapter);
            }

            @Override
            public void onFailure(Call<OrderBook> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
