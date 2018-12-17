package com.id.yourway.activities;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

import com.id.yourway.R;

public class PreferencesActivity extends AppCompatActivity {

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Switch vibrateSwitch = findViewById(R.id.VibrateSwitch);

        vibrateSwitch.setOnClickListener(view -> {
            if (vibrateSwitch.isActivated()) {
                // Code to turn vibrate off
                vibrator.cancel();
            } else if (!vibrateSwitch.isActivated()) {
                // Code to turn vibrate on
                vibrator.vibrate(100);
            }
        });
    }
}
