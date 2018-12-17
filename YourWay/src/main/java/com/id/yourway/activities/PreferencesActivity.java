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

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Switch vibrateSwitch = findViewById(R.id.VibrateSwitch);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }


        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
                if (vibrateSwitch.isEnabled()) {
                    aManager.setRingerMode(aManager.VIBRATE_SETTING_ONLY_SILENT);
                    System.out.println("nee");
                }
            }
        });
    }
}
