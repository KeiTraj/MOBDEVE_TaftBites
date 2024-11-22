package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find the back button by its ID
        ImageButton backButton = findViewById(R.id.back_button);

        // Set an OnClickListener to handle the back button press
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                finish();  // Closes this activity and returns to the previous one
            }
        });
    }
}
