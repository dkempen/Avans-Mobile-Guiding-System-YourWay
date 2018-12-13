package com.id.yourway.business.feedback;

import android.content.Context;

public class FeedbackManager {

    private final HapticManager hapticManager;
    private final SoundManager soundManager;
    private final ToastManager toastManager;

    public FeedbackManager() {
        hapticManager = new HapticManager();
        soundManager = new SoundManager();
        toastManager = new ToastManager();
    }

    public void onNearbySight(Context context) {
        hapticManager.vibrate(context);
        soundManager.playSound(context);
    }

    public void onGPSLost(Context context) {
        hapticManager.vibrate(context);
        toastManager.displayToast(context, "GPS Connection Lost", true);
    }

    public void onInternetLost(Context context) {
        hapticManager.vibrate(context);
        toastManager.displayToast(context, "Internet Connection Lost", true);
    }

    public void onError(Context context, String message) {
        hapticManager.vibrate(context);
        soundManager.playSound(context);
        toastManager.displayToast(context, message, true);
    }
}
