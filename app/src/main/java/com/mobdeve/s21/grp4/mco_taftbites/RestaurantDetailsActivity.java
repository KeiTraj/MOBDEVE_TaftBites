package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore instance
    private List<MenuItem> menuItemsList; // List for menu items
    private MenuAdapter menuAdapter; // Adapter for menu RecyclerView
    private List<Review> reviewsList; // List for reviews
    private ReviewAdapter reviewAdapter; // Adapter for reviews RecyclerView
    private ImageView restaurantImage; // Restaurant Image
    private TextView restaurantName; // Restaurant Name
    private TextView restaurantAddress; // Restaurant Address
    private TextView restaurantRating; // Restaurant Rating
    private TextView restaurantTiming; // Restaurant Timing
    private TextView restaurantPrice; // Restaurant Price
    private ImageView nextButton; // Next button for "What People Say"

    private String restaurantId; // Restaurant ID passed from MainPageActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details); // Ensure this matches your layout

        // Retrieve restaurant ID from intent
        restaurantId = getIntent().getStringExtra("restaurantId");

        if (restaurantId == null || restaurantId.isEmpty()) {
            Log.e("RestaurantDetails", "No restaurant ID passed to RestaurantDetailsActivity");
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        restaurantImage = findViewById(R.id.restaurantThumbnailImage);
        restaurantName = findViewById(R.id.restaurantName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantRating = findViewById(R.id.restaurantRating);
        restaurantTiming = findViewById(R.id.openHours);
        restaurantPrice = findViewById(R.id.priceLevel);

        // Initialize RecyclerView for menu items
        RecyclerView menuRecyclerView = findViewById(R.id.menuRecyclerView);
        LinearLayoutManager menuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(menuLayoutManager);

        menuItemsList = new ArrayList<>();
        menuAdapter = new MenuAdapter(menuItemsList, this);
        menuRecyclerView.setAdapter(menuAdapter);

        // Initialize RecyclerView for reviews
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        reviewsList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewsList, this);
        reviewRecyclerView.setAdapter(reviewAdapter);

        // Set up the next button for reviews
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailsActivity.this, ReviewRatingsReviews_DetailsActivity.class);
            intent.putExtra("restaurantId", restaurantId); // Pass restaurant ID for detailed reviews
            startActivity(intent);
        });

        // Fetch restaurant details
        fetchRestaurantDetails();

        // Fetch menu items
        fetchMenuItems();

        // Fetch reviews
        fetchReviews();
    }


    private void fetchRestaurantDetails() {
        db.collection("restaurants").document(restaurantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Set restaurant details
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
                        restaurantRating.setText(rating != null ? String.valueOf(rating) : "Rating Unavailable");
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
                        // Fetch menu items from Firestore map structure
                        List<Map<String, Object>> menuList = (List<Map<String, Object>>) documentSnapshot.get("menu");
                        menuItemsList.clear();
                        for (Map<String, Object> menuItemMap : menuList) {
                            String name = (String) menuItemMap.get("name");
                            String imageUrl = (String) menuItemMap.get("imageUrl");
                            menuItemsList.add(new MenuItem(name, imageUrl));
                        }
                        menuAdapter.notifyDataSetChanged(); // Update RecyclerView
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching menu items", e));
    }

    private void fetchReviews() {
        db.collection("restaurants").document(restaurantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("reviews")) {
                        // Clear existing reviews list
                        reviewsList.clear();

                        // Fetch the array of reviews from Firestore
                        List<Map<String, Object>> reviewsArray = (List<Map<String, Object>>) documentSnapshot.get("reviews");
                        if (reviewsArray != null) {
                            for (Map<String, Object> reviewMap : reviewsArray) {
                                String reviewText = (String) reviewMap.get("reviewText");
                                String reviewerName = (String) reviewMap.get("reviewerName");
                                String reviewRatingStr = (String) reviewMap.get("reviewRating");
                                float reviewRating = reviewRatingStr != null ? Float.parseFloat(reviewRatingStr) : 0;

                                reviewsList.add(new Review(reviewerName, reviewText, reviewRating));
                            }
                        }

                        // Notify adapter of data changes
                        reviewAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching reviews", e));
    }

}
