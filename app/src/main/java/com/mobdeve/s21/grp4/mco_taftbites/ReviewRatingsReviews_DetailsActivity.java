package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewRatingsReviews_DetailsActivity extends AppCompatActivity {

    private RecyclerView reviewsRecyclerView;
    private ReviewRatingsReviewAdapter reviewAdapter;
    private List<ReviewRatingsReview> reviewList;
    private ImageButton backButton;
    private LinearLayout writeReviewButton;
    private FirebaseFirestore db; // Firestore instance
    private String restaurantId; // Restaurant ID from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_review_details);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get the restaurant ID passed from the previous activity
        restaurantId = getIntent().getStringExtra("restaurantId");

        if (restaurantId == null || restaurantId.isEmpty()) {
            Log.e("ReviewDetails", "No restaurant ID provided!");
            finish();
            return;
        }

        // Initialize RecyclerView
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Back Button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Initialize Write Review Button
        writeReviewButton = findViewById(R.id.write_review_button);
        writeReviewButton.setOnClickListener(v -> {
            // Navigate to a form activity for writing reviews
            Intent intent = new Intent(ReviewRatingsReviews_DetailsActivity.this, RateReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId); // Pass restaurant ID to RateReviewActivity
            startActivity(intent);
        });

        // Initialize review list and adapter
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewRatingsReviewAdapter(reviewList, this);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        // Fetch reviews from Firestore
        fetchReviewsFromFirebase();
    }

    private void fetchReviewsFromFirebase() {
        db.collection("restaurants")
                .document(restaurantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("reviews")) {
                        // Fetch reviews array directly from the restaurant document
                        List<Map<String, Object>> reviewsArray = (List<Map<String, Object>>) documentSnapshot.get("reviews");
                        reviewList.clear();

                        for (Map<String, Object> reviewMap : reviewsArray) {
                            // Parse review fields
                            String reviewerName = (String) reviewMap.get("reviewerName");
                            String reviewText = (String) reviewMap.get("reviewText");
                            String reviewDate = (String) reviewMap.get("reviewDate");
                            String profileImageUrl = (String) reviewMap.get("profileImageUrl");
                            String reviewRatingStr = (String) reviewMap.get("reviewRating");
                            float reviewRating = reviewRatingStr != null ? Float.parseFloat(reviewRatingStr) : 0;

                            // Add review to the list
                            reviewList.add(new ReviewRatingsReview(
                                    reviewerName,
                                    reviewText,
                                    reviewDate,
                                    profileImageUrl, // Pass the URL directly
                                    reviewRating
                            ));
                        }
                        reviewAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching reviews", e));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchReviewsFromFirebase(); // Refresh the reviews list when returning to the activity
    }
}
