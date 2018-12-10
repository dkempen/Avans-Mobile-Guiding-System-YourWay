package com.id.yourway.business;

import android.content.Context;
import android.media.MediaPlayer;

import com.id.yourway.R;

class SoundManager {
    void playSound(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.notification);
        player.start();
    }
}
