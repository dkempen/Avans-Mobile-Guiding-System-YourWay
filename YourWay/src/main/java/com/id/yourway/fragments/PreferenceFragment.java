package com.id.yourway.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.id.yourway.R;
import com.id.yourway.activities.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceFragment extends Fragment {

    Vibrator vibrator;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v = inflater.inflate(R.layout.fragment_preference, null, false);
        Switch vibrateSwitch = v.findViewById(R.id.settingsVibrateSwitch);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        vibrateSwitch.setChecked(prefs.getBoolean("vibrate", true));

        vibrateSwitch.setOnClickListener(view ->
                prefs.edit().putBoolean("vibrate", vibrateSwitch.isChecked()).apply());
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setToolbarSettingsEnabled(false);
        mainActivity.setToolbarTitle("Your Way");
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setToolbarSettingsEnabled(true);
        mainActivity.setToolbarTitle("Your Way");
    }
}
