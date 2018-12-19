package com.id.yourway.business.feedback;

import android.content.Context;

import com.id.yourway.R;

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
        toastManager.displayToast(context, context.getString(R.string.feedbackOnGpsLost), true);
    }

    public void onInternetLost(Context context) {
        hapticManager.vibrate(context);
        toastManager.displayToast(context, context.getString(R.string.feedbackOnInternetLost), true);
    }

    public void onError(Context context, String message) {
        hapticManager.vibrate(context);
        soundManager.playSound(context);
        toastManager.displayToast(context, message, true);
    }

    public void onRouteFinished(Context context) {
        hapticManager.vibrate(context);
        soundManager.playSound(context);
        toastManager.displayToast(context, context.getString(R.string.feedbackOnRouteFinish), true);
    }
}
