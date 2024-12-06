package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<MenuItem> menuItemsList;
    private MenuAdapter menuAdapter;
    private List<Review> reviewsList;
    private ReviewAdapter reviewAdapter;
    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantRating;
    private TextView restaurantTiming;
    private TextView restaurantPrice;
    private ImageView nextButton;

    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restaurantId = getIntent().getStringExtra("restaurantId");

        if (restaurantId == null || restaurantId.isEmpty()) {
            Log.e("RestaurantDetails", "No restaurant ID passed to RestaurantDetailsActivity");
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        restaurantImage = findViewById(R.id.restaurantThumbnailImage);
        restaurantName = findViewById(R.id.restaurantName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantRating = findViewById(R.id.restaurantRating);
        restaurantTiming = findViewById(R.id.openHours);
        restaurantPrice = findViewById(R.id.priceLevel);

        RecyclerView menuRecyclerView = findViewById(R.id.menuRecyclerView);
        LinearLayoutManager menuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(menuLayoutManager);

        menuItemsList = new ArrayList<>();
        menuAdapter = new MenuAdapter(menuItemsList, this);
        menuRecyclerView.setAdapter(menuAdapter);

        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        reviewsList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewsList, this);
        reviewRecyclerView.setAdapter(reviewAdapter);

        nextButton = findViewById(R.id.nextButton);
        // Next Button for "Rate Review"
        nextButton.setOnClickListener(v -> {
            if (restaurantId == null || restaurantId.isEmpty()) {
                Log.e("RestaurantDetailsActivity", "Restaurant ID is missing!");
                Toast.makeText(this, "Cannot proceed without restaurant information.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(RestaurantDetailsActivity.this, RateReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId); // Pass restaurant ID
            startActivity(intent);
        });


        fetchRestaurantDetails();
        fetchMenuItems();
        fetchReviews();
    }

    private void fetchRestaurantDetails() {
        db.collection("restaurants").document(restaurantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        String name = documentSnapshot.getString("name");
                        String address = documentSnapshot.getString("address");
                        Double rating = documentSnapshot.getDouble("rating");
                        String timing = documentSnapshot.getString("time");
                        String price = documentSnapshot.getString("price");

                        if (imageUrl != null) {
                            Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error_image)
                                    .into(restaurantImage);
                        }
                        restaurantName.setText(name != null ? name : "Restaurant Name Unavailable");
                        restaurantAddress.setText(address != null ? address : "Address Unavailable");
                        restaurantRating.setText(rating != null ? String.format("%.1f", rating) : "Rating Unavailable");
                        restaurantTiming.setText(timing != null ? timing : "Opening Hours Unavailable");
                        restaurantPrice.setText(price != null ? price : "Price Range Unavailable");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching restaurant details", e));
    }

    private void fetchMenuItems() {
        db.collection("restaurants").document(restaurantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("menu")) {
                        List<Map<String, Object>> menuList = (List<Map<String, Object>>) documentSnapshot.get("menu");
                        menuItemsList.clear();
                        for (Map<String, Object> menuItemMap : menuList) {
                            String name = (String) menuItemMap.get("name");
                            String imageUrl = (String) menuItemMap.get("imageUrl");
                            menuItemsList.add(new MenuItem(name, imageUrl));
                        }
                        menuAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching menu items", e));
    }

    private void fetchReviews() {
        db.collection("restaurants").document(restaurantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("reviews")) {
                        reviewsList.clear();

                        List<Map<String, Object>> reviewsArray = (List<Map<String, Object>>) documentSnapshot.get("reviews");
                        if (reviewsArray != null) {
                            for (Map<String, Object> reviewMap : reviewsArray) {
                                String reviewText = (String) reviewMap.get("reviewText");
                                String reviewerName = (String) reviewMap.get("reviewerName");

                                // Safely handle reviewRating to avoid ClassCastException
                                Object reviewRatingObj = reviewMap.get("reviewRating");
                                float reviewRating = 0;
                                if (reviewRatingObj instanceof Double) {
                                    reviewRating = ((Double) reviewRatingObj).floatValue();
                                } else if (reviewRatingObj instanceof String) {
                                    try {
                                        reviewRating = Float.parseFloat((String) reviewRatingObj);
                                    } catch (NumberFormatException e) {
                                        Log.e("FetchReviews", "Invalid reviewRating format: " + reviewRatingObj, e);
                                    }
                                }

                                reviewsList.add(new Review(reviewerName, reviewText, reviewRating));
                            }
                        }

                        reviewAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching reviews", e));
    }
}
