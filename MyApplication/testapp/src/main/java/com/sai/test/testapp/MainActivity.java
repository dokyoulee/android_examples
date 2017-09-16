package com.sai.test.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_widget_test1,
            R.id.button_intent_test,
            R.id.button_service_test,
            R.id.button_provider_test,
            R.id.button_media_test})
    void handleButtonClock(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button_widget_test1:
                intent = new Intent(this, WidgetTestActivity1.class);
                startActivity(intent);
                break;
            case R.id.button_intent_test:
                intent = new Intent(this, IntentTestActivity.class);
                startActivity(intent);
                break;
            case R.id.button_service_test:
                intent = new Intent(this, ServiceTestActivity.class);
                startActivity(intent);
                break;
            case R.id.button_provider_test:
                intent = new Intent(this, ProviderTestActivity.class);
                startActivity(intent);
                break;
            case R.id.button_media_test:
                intent = new Intent(this, MediaTestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
