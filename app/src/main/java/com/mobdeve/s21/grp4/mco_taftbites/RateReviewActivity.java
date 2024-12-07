package com.mobdeve.s21.grp4.mco_taftbites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RateReviewActivity extends AppCompatActivity {

    private ImageButton backButton;
    private RatingBar ratingBar;
    private EditText reviewInput;
    private Button postButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String restaurantId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the restaurant ID passed from the previous activity
        restaurantId = getIntent().getStringExtra("restaurantId");

        // Debugging log for restaurantId
        Log.d("RateReviewActivity", "Restaurant ID: " + restaurantId);

        if (restaurantId == null || restaurantId.isEmpty()) {
            Toast.makeText(this, "Restaurant ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        backButton = findViewById(R.id.backbutton);
        ratingBar = findViewById(R.id.ratingBar);
        reviewInput = findViewById(R.id.reviewInput);
        postButton = findViewById(R.id.postButton);

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting your review...");
        progressDialog.setCancelable(false);

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

        // Get review details
        String reviewerName = mAuth.getCurrentUser().getDisplayName();
        if (reviewerName == null || reviewerName.isEmpty()) {
            reviewerName = "Anonymous";
        }
        String reviewText = reviewInput.getText().toString();
        float rating = ratingBar.getRating();

        // Validate input
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("RateReviewActivity", "Review Details: Name=" + reviewerName + ", Text=" + reviewText + ", Rating=" + rating);

        // Create review object
        Map<String, Object> review = new HashMap<>();
        review.put("reviewerName", reviewerName);
        review.put("reviewText", reviewText);
        review.put("reviewDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        review.put("reviewRating", rating);

        // Show Progress Dialog
        progressDialog.show();

        // Check if restaurant document exists
        db.collection("restaurants")
                .document(restaurantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("RateReviewActivity", "Restaurant found: " + restaurantId);

                        // Add the review to the reviews array
                        db.collection("restaurants")
                                .document(restaurantId)
                                .update("reviews", FieldValue.arrayUnion(review))
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(RateReviewActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Log.e("Firestore", "Error posting review", e);
                                    Toast.makeText(RateReviewActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        progressDialog.dismiss();
                        Log.e("RateReviewActivity", "Restaurant not found");
                        Toast.makeText(RateReviewActivity.this, "Restaurant not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e("Firestore", "Error fetching restaurant", e);
                    Toast.makeText(RateReviewActivity.this, "Failed to fetch restaurant data", Toast.LENGTH_SHORT).show();
                });
    }


}
