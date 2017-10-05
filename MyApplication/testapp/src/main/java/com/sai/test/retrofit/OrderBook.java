package com.sai.test.retrofit;

import java.util.List;

/**
 * Created by sai on 2017-10-04.
 */

public class OrderBook {
    public List<Order> bid;
    public List<Order> ask;

    public class Order {
        public long price;
        public double qty;
    }
}

