package com.example.csp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by csp on 2016-05-29.
 */
public class startActivity  extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);


        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
              /*  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
              */
                finish();
            }
        }, 2000);
    }

}

