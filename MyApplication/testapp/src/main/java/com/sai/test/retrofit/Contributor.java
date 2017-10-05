package com.sai.test.retrofit;

/**
 * Created by sai on 2017-10-03.
 */

public class Contributor {
    String login;
    String html_url;
    int contributions;

    @Override
    public String toString() {
        return login + "(" + contributions + ") - " + html_url;
    }
}
