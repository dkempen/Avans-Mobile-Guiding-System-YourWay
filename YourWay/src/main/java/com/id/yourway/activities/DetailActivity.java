package com.id.yourway.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.id.yourway.R;
import com.id.yourway.adapters.ViewPagerAdapter;
import com.id.yourway.entities.Sight;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DetailActivity extends AppCompatActivity {

    TextView muralAuthorName;
    TextView muralDescription;
    TextView muralYear;
    Sight sight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        muralAuthorName = findViewById(R.id.detailedActivity_muralName);
        muralDescription = findViewById(R.id.detailedActivity_muralDescription);
        muralYear = findViewById(R.id.detailedActivity_muralYear);

        Intent intent = getIntent();
        sight = (Sight) intent.getSerializableExtra("SIGHT_OBJECT");

        muralDescription.setMovementMethod(new ScrollingMovementMethod());
        muralDescription.setText(sight.getDescription());
        muralAuthorName.setText(sight.getAuthor());

        Date date = new Date(1485263473L * 1000);
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String formatted = jdf.format(date);

        muralYear.setText(String.valueOf(formatted));

        List<String> list = sight.getImages();
        String[] stringArray = (String[]) list.toArray(new String[list.size()]);

        ViewPager viewPager = findViewById(R.id.detailledviewPagerID);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, stringArray);
        viewPager.setAdapter(adapter);
    }
}