package com.id.yourway.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.id.yourway.R;

public class PreferencesActivity extends AppCompatActivity {

    public static boolean Vibrate = true;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Switch vibrateSwitch = findViewById(R.id.VibrateSwitch);

        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
                if (vibrateSwitch.isEnabled()) {
                    Vibrate = false;
                    System.out.println("nee");
                }
            }
        });
    }

    public static boolean isVibrate() {
        return Vibrate;
    }
}
