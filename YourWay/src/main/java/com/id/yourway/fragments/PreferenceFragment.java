package com.id.yourway.fragments;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.id.yourway.R;

public class PreferenceFragment extends Fragment {
    public static boolean Vibrate = true;
    Vibrator vibrator;
View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v = inflater.inflate(R.layout.fragment_preference, null, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.settings_image);
        Switch vibrateSwitch = v.findViewById(R.id.vibrate_switch);
        vibrateSwitch.setOnClickListener(view -> {
            if (vibrateSwitch.isEnabled()) {
                Vibrate = false;
                System.out.println("nee");
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static boolean isVibrate() {
        return Vibrate;
    }
}
