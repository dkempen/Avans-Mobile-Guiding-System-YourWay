package com.id.yourway.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.id.yourway.R;

public class PreferencesActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Switch vibrateSwitch = (Switch) findViewById(R.id.VibrateSwitch);
        Switch ColorBlind = (Switch) findViewById(R.id.ColorBlindSwitch);

        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrateSwitch.isActivated()){
                    //Code om het vibrate uit te zetten
                }else if (!vibrateSwitch.isActivated()){
                    //Code om het vibrate aan te zetten
                }
            }
        });

        ColorBlind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ColorBlind.isActivated()){
                    //Code om het vibrate uit te zetten
                }else if (!ColorBlind.isActivated()){
                    //Code om het vibrate aan te zetten
                }
            }
        });


    }
}
