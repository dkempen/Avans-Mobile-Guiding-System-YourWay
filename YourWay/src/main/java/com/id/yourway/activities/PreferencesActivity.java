package com.id.yourway.activities;

import android.content.Context;
import android.media.AudioManager;
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

        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
                if (vibrateSwitch.isEnabled()) {
                    aManager.setRingerMode(aManager.RINGER_MODE_SILENT);
                    System.out.println("nee");
                }
            }
        });

    }
}
