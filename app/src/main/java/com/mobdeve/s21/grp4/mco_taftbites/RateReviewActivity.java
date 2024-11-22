package com.mobdeve.s21.grp4.mco_taftbites;
// This activity allows users to leave a rating and review.
// RATE & REVIEW PAGE

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class RateReviewActivity extends AppCompatActivity {
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        // Initialize Back Button
        backButton = findViewById(R.id.backbutton);

        // Set Back Button functionality
        backButton.setOnClickListener(v -> finish());
    }
}
