package com.id.yourway.business.feedback;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

class ToastManager {
    void displayToast(Context context, String message, int type, boolean longTime) {
        FancyToast.makeText(context, message, longTime ? FancyToast.LENGTH_LONG :
                FancyToast.LENGTH_SHORT, type, false).show();
    }
}
