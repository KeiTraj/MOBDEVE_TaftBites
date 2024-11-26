package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);  // Connects to your XML layout

        // Reference to the views
        ImageView logo = findViewById(R.id.logo);
        TextView description = findViewById(R.id.splashDescription);
        Button getStartedButton = findViewById(R.id.getStartedBtn);

        // Initially hide description and button
        description.setVisibility(View.INVISIBLE);
        getStartedButton.setVisibility(View.INVISIBLE);

        // Load the fade-in animation from res/anim/fade_in.xml
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Show the logo immediately with fade-in
        logo.startAnimation(fadeIn);

        // Show the description text after 1.5 seconds with fade-in
        new Handler().postDelayed(() -> {
            description.setVisibility(View.VISIBLE);
            description.startAnimation(fadeIn);  // Apply fade-in animation to the description
        }, 1500);  // 1.5 seconds delay

        // Show the "Get Started" button after 3 seconds with fade-in
        new Handler().postDelayed(() -> {
            getStartedButton.setVisibility(View.VISIBLE);
            getStartedButton.startAnimation(fadeIn);  // Apply fade-in animation to the button
        }, 3000);  // 3 seconds delay

        // Set up the "Get Started" button to navigate to LoginActivity
        getStartedButton.setOnClickListener(v -> navigateToLogin());
    }

    // Navigate to LoginActivity
    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Close SplashActivity to prevent it from appearing again
    }
}