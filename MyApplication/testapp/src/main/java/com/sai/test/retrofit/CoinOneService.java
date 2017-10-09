package com.sai.test.retrofit;

import android.util.Base64;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sai on 2017-10-04.
 */

public interface CoinOneService {
    String BASE_URL = "https://api.coinone.co.kr/";
    String ACCESS_TOKEN = "";
    String SEC_KEY = "";
    String SEC_ALGO = "HmacSHA512";

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // generate payload
                /*
                 * http://doc.coinone.co.kr/#api-Account_V2-UserBalance
                 * X-COINONE-PAYLOAD        Parameter object -> JSON string -> base64
                 * X-COINONE-SIGNATURE      HMAC(X-COINONE-PAYLOAD, SECRET_KEY, SHA512).hexdigest()
                 */
                AuthParameter param = new AuthParameter();
                param.nonce = (int)System.currentTimeMillis();
                String json = new Gson().toJson(param);
                String payload = Base64.encodeToString(json.getBytes("UTF-8"), Base64.NO_WRAP);

                // generate signature
                Mac sha512_hmac = null;
                String signature = "";
                try {
                    sha512_hmac = Mac.getInstance(SEC_ALGO);
                    SecretKeySpec sec_key = new SecretKeySpec(SEC_KEY.getBytes("UTF-8"), SEC_ALGO);
                    sha512_hmac.init(sec_key);
                    signature = new String(Hex.encodeHex(sha512_hmac.doFinal(payload.getBytes("UTF-8"))));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

                Request request = chain.request().newBuilder()
                        .header("content-type", "application/json")
                        .header("X-COINONE-PAYLOAD", payload)
                        .header("X-COINONE-SIGNATURE", signature)
                        .build();

                return chain.proceed(request);
            }
        }).build();

    Retrofit retrofitPublic = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofitPrivate = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    List<String> currencyList = new ArrayList<>(Arrays.asList(new String[] {"btc", "eth", "etc"}));

    @GET("orderbook")
    Call<OrderBook> getOrderBooks(@Query("currency") String currency);

    @POST("v2/account/balance")
    Call<Balance> getBalance();

    class AuthParameter {
        String access_token = ACCESS_TOKEN;
        Integer nonce;
    }
}
