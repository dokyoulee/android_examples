package com.sai.test.testapp;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sai.test.RecyclerView.RecyclerViewAdapter;
import com.sai.test.retrofit.OrderBook;
import com.sai.test.retrofit.OrderBookService;
import com.sai.test.testapp.databinding.ActivityRetrofitBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 100;
    private static final String RECEIVER_PERMISSION = "android.permission.INTERNET";

    ActivityRetrofitBinding mBinding;
    RecyclerViewAdapter mOrderBookAdapter;
    OrderBookService mOrderBookService;
    Callback mOrderBookCallback = new Callback<OrderBook>() {
        @Override
        public void onResponse(Call<OrderBook> call, Response<OrderBook> response) {
            mOrderBookAdapter.setData(response.body().bid);
            mOrderBookAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<OrderBook> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retrofit);
        mBinding.content.setVariable(BR.retrofitActivity, this);
        setSupportActionBar(mBinding.toolbar);
        mBinding.content.rvOutput.setLayoutManager(new LinearLayoutManager(this));
        mOrderBookService = OrderBookService.retrofit.create(OrderBookService.class);
        mBinding.content.spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, OrderBookService.currencyList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrderBookAdapter = new RecyclerViewAdapter<>(R.layout.orderbook_item, BR.order, null);
        mBinding.content.rvOutput.setAdapter(mOrderBookAdapter);
        requestPermission(RECEIVER_PERMISSION);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.button_run) {
            queryOrderBookData(mBinding.content.spinner.getSelectedItem().toString());
        }
    }

    private void queryOrderBookData(String currency) {
        Call<OrderBook> call = mOrderBookService.getOrderBooks(currency);
        call.enqueue(mOrderBookCallback);
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
