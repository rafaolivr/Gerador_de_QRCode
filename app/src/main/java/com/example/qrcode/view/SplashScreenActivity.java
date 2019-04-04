package com.example.qrcode.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qrcode.R;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Time de Loading para inicializar o aplicativo
        Thread timeThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, TelaInicialActivity.class);
                    startActivity(intent);
                }
            }
        };
        timeThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
