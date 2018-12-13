package com.id.yourway.views;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.id.yourway.R;
import com.id.yourway.activities.MainActivity;
import com.id.yourway.activities.PreferencesActivity;
import com.id.yourway.fragments.HelpFragment;

public class ToolbarFactory {

    static void createToolbar(Toolbar toolbar, String className) {
        AppCompatActivity activity =  (AppCompatActivity) toolbar.getContext();
        activity.setSupportActionBar(toolbar);

        Toolbar mToolbar = activity.findViewById(R.id.tool_bar);
        mToolbar.bringToFront();
        ImageButton searchButton = activity.findViewById(R.id.toolBarSearchButton);
        searchButton.setVisibility(View.INVISIBLE);

        activity.setSupportActionBar(mToolbar);
        // Hide the title
        activity.getSupportActionBar().setTitle(null);

        ImageButton settingsButton = activity.findViewById(R.id.toolBarSettingsButton);
//        settingsButton.setOnClickListener(v ->
//                activity.startActivity(new Intent(MainActivity.this, PreferencesActivity.class)));
//
//        ImageButton hamburgerButton = activity.findViewById(R.id.toolBarHamburgerButton);
//        hamburgerButton.setOnClickListener(v -> {
//            if (mDrawerLayout.isDrawerOpen(Gravity.START))
//                mDrawerLayout.closeDrawer(Gravity.START);
//            else
//                mDrawerLayout.openDrawer(Gravity.START);
//        });
//
//        ImageButton helpButton = findViewById(R.id.toolBarHelpButton);
//        helpButton.setOnClickListener(v -> {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            HelpFragment helpFragment = new HelpFragment();
//            helpFragment.show(ft, "HELP");
//        });
    }
}
