package com.mobdeve.s21.grp4.mco_taftbites;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    private FirebaseFirestore db; // Firestore instance
    private List<RestaurantItem> restaurantList; // List to hold restaurant data
    private RestaurantAdapter adapter; // Adapter for RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page); // Ensure this matches the correct layout file

        // Customize the filter button
        customizeFilterButton();

        // Initialize Firestore and RecyclerView
        initializeFirestore();
        initializeRecyclerView();

        // Fetch restaurant data from Firestore
        fetchRestaurantsFromFirestore();
    }

    /**
     * Customizes the filter button appearance.
     */
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    private void customizeFilterButton() {
        Button filterButton = findViewById(R.id.filter_button); // Use the correct button ID
        filterButton.setBackgroundColor(Color.parseColor("#8BC34A")); // Set a custom background color
    }

    /**
     * Initializes the Firestore instance.
     */
    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance(); // Get Firestore instance
    }

    /**
     * Initializes the RecyclerView and its adapter.
     */
    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.restaurantRV); // RecyclerView for restaurants
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LinearLayoutManager
        restaurantList = new ArrayList<>(); // Initialize empty restaurant list
        adapter = new RestaurantAdapter(this, restaurantList); // Initialize adapter
        recyclerView.setAdapter(adapter); // Set adapter to RecyclerView
    }

    /**
     * Fetches restaurant data from Firestore and updates the RecyclerView.
     */
    private void fetchRestaurantsFromFirestore() {
        db.collection("restaurants") // Firestore collection name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        restaurantList.clear(); // Clear list to avoid duplicates
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                // Map Firestore document to RestaurantItem
                                RestaurantItem restaurant = new RestaurantItem(
                                        document.getId(),
                                        document.getString("name"),
                                        document.getDouble("rating").floatValue(),
                                        document.getString("distance"),
                                        document.getString("cuisineType"),
                                        document.getString("imageUrl")
                                );
                                restaurantList.add(restaurant); // Add restaurant to the list
                            } catch (Exception e) {
                                Log.e("Firestore", "Error mapping document: " + document.getId(), e);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter about updated data
                    } else {
                        Log.e("Firestore", "Error fetching documents", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching data", e)); // Handle failure
    }
}
