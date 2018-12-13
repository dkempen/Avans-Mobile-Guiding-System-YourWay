package com.id.yourway.business;

import android.content.Context;

public class FeedbackManager {

    private final HapticManager hapticManager;
    private final SoundManager soundManager;

    public FeedbackManager() {
        hapticManager = new HapticManager();
        soundManager = new SoundManager();
    }

    public void onNearbySight(Context context) {
        hapticManager.vibrate(context);
        soundManager.playSound(context);
    }
}
