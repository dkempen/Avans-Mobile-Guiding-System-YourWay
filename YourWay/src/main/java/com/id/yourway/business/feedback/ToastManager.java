package com.id.yourway.business.feedback;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

class ToastManager {
    void displayToast(Context context, String message, boolean longTime) {
        FancyToast.makeText(context, message, FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
    }
}
