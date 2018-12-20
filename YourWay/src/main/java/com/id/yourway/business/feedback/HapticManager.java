package com.id.yourway.business.feedback;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.id.yourway.fragments.PreferenceFragment;


class HapticManager {
    void vibrate(Context context) {
        new Thread(() -> {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            int loops = 3;
            int delay = 250;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && PreferenceFragment.isVibrate()) {
                for (int i = 0; i < loops; i++) {
                    v.vibrate(VibrationEffect.createOneShot(delay, VibrationEffect.DEFAULT_AMPLITUDE));
                    try {
                        Thread.sleep(delay * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < loops; i++) {
                    v.vibrate(delay);
                    try {
                        Thread.sleep(delay * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
