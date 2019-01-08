package com.id.yourway.business.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;

class HapticManager {
    void vibrate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getBoolean("vibrate", true))
            return;

        new Thread(() -> {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            int loops = 3;
            int delay = 250;

            for (int i = 0; i < loops; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    v.vibrate(VibrationEffect.createOneShot(delay, VibrationEffect.DEFAULT_AMPLITUDE));
                else
                    v.vibrate(delay);
                try {
                    Thread.sleep(delay * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
