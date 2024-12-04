package com.mobdeve.s21.grp4.mco_taftbites;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso; // Import Picasso for image loading

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize the views
        profileImage = findViewById(R.id.profile_image);  // Profile image
        profileName = findViewById(R.id.profile_name);    // Profile name
        profileEmail = findViewById(R.id.profile_email);  // Profile email
        backButton = findViewById(R.id.back_button);      // Back button

        // Get the data passed from MainPageActivity
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String profileImageUrl = getIntent().getStringExtra("profileImageUrl");

        // Set the values in the UI
        profileName.setText(username);
        profileEmail.setText(email);

        // Use Picasso to load the profile image
        Picasso.get().load(profileImageUrl).into(profileImage);

        // Set the back button functionality
        backButton.setOnClickListener(v -> finish());  // Close the activity and return to the previous one
    }
}
