package com.id.yourway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.id.yourway.R;
import com.id.yourway.adapters.ViewPagerAdapter;
import com.id.yourway.entities.Sight;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    Toolbar muralTitle;
    TextView muralAuthorName;
    TextView muralPhotographer;
    TextView muralDescription;
    TextView muralYear;
    Sight sight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        muralTitle = findViewById(R.id.toolbar_detail);
        muralAuthorName = findViewById(R.id.detailedActivity_author);
        muralPhotographer = findViewById(R.id.detailedActivity_photographer);
        muralDescription = findViewById(R.id.detailedActivity_muralDescription);
        muralYear = findViewById(R.id.detailedActivity_year);

        Intent intent = getIntent();
        sight = (Sight) intent.getSerializableExtra("SIGHT_OBJECT");
        muralDescription.setText(sight.getDescription());
        muralAuthorName.setText(sight.getAuthor());
        muralPhotographer.setText(sight.getPhotographer());

        setSupportActionBar(muralTitle);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(sight.getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Date date = new Date(1485263473L * 1000);
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String formatted = jdf.format(date);

        muralYear.setText(String.valueOf(formatted));

        List<String> list = sight.getImages();
        String[] stringArray = list.toArray(new String[0]);

        ViewPager viewPager = findViewById(R.id.detailledviewPagerID);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, stringArray, sight.getType());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
