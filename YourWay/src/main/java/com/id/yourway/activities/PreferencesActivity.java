package com.id.yourway.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.id.yourway.R;

public class PreferencesActivity  extends AppCompatActivity {

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Switch vibrateSwitch = (Switch) findViewById(R.id.VibrateSwitch);
        Switch ColorBlind = (Switch) findViewById(R.id.ColorBlindSwitch);

        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrateSwitch.isActivated()){
                    //Code om het vibrate uit te zetten
                    vibrator.cancel();
                }else if (!vibrateSwitch.isActivated()){
                    //Code om het vibrate aan te zetten
                    vibrator.vibrate(100);
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
