package com.id.yourway.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.id.yourway.R;

public class DetailActivity extends AppCompatActivity {

    TextView muralAuthorName;
    TextView muralDescription;
    TextView muralYear;
    ImageView muralImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        muralAuthorName = findViewById(R.id.detailedActivity_muralName);
        muralDescription = findViewById(R.id.detailedActivity_muralDescription);
        muralYear = findViewById(R.id.detailedActivity_muralYear);
        muralImage = findViewById(R.id.detailedActivity_muralImage);
    }
}
