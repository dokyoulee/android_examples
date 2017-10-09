package com.sai.test.retrofit;

/**
 * Created by sai on 2017-10-08.
 */

public class Balance {
    public Currency eth;
    public Currency krw;

    public class Currency {
        double avail;
        double balance;

        @Override
        public String toString() {
            return "available : " + String.format("%.1f", avail) + ", balance : " + String.format("%.1f", balance) ;
        }
    }
}
