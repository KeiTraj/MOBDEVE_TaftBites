package com.mobdeve.s21.grp4.mco_taftbites;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RateReviewActivity extends AppCompatActivity {

    private ImageButton backButton;
    private RatingBar ratingBar;
    private EditText reviewInput;
    private Button postButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the restaurant ID passed from the previous activity
        restaurantId = getIntent().getStringExtra("restaurantId");

        // Initialize UI elements
        backButton = findViewById(R.id.backbutton);
        ratingBar = findViewById(R.id.ratingBar);
        reviewInput = findViewById(R.id.reviewInput);
        postButton = findViewById(R.id.postButton);

        // Set Back Button functionality
        backButton.setOnClickListener(v -> finish());

        // Handle Post Review Button click
        postButton.setOnClickListener(v -> postReview());
    }

    private void postReview() {
        // Ensure user is logged in
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "You need to be logged in to submit a review", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get review details from the UI
        String reviewerName = mAuth.getCurrentUser().getDisplayName(); // Get the display name of the logged-in user
        String reviewText = reviewInput.getText().toString();
        float rating = ratingBar.getRating();

        if (reviewText.isEmpty()) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create review object
        Map<String, Object> review = new HashMap<>();
        review.put("reviewerName", reviewerName);
        review.put("reviewText", reviewText);
        review.put("reviewDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // Current date
        review.put("reviewRating", rating);
        review.put("profileImageUrl", "http://example.com/profile_image.jpg"); // Replace with the actual URL or use a placeholder

        // Add the review to the reviews array in Firestore
        db.collection("restaurants")
                .document(restaurantId)
                .update("reviews", FieldValue.arrayUnion(review)) // Add the review to the array
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RateReviewActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after posting the review
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error posting review", e);
                    Toast.makeText(RateReviewActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                });
    }


}
