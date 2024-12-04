package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    Button facebook_login;
    private FirebaseFirestore db;
    private List<RestaurantItem> restaurantList;
    private List<RestaurantItem> filteredList;
    private RestaurantAdapter adapter;
    CallbackManager callbackManager;
    ImageView profileImage; // Declare the ImageView
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //startActivity(new Intent());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        // Retrieve user details from Intent
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String profileImageUrl = getIntent().getStringExtra("profileImageUrl"); // Add this line to get profileImageUrl
        Log.d("MainPageActivity", "Logged-in User: " + username + " (" + email + ")");

        // Initialize the profile image ImageView
        profileImage = findViewById(R.id.profileImage); // Connect the ImageView from XML

        // Load profile image dynamically if needed
        Picasso.get().load(profileImageUrl).into(profileImage); // Dynamically load profile image

        // Set an OnClickListener for profile image to go to the ProfileActivity
        profileImage.setOnClickListener(v -> {
            // Intent to navigate to ProfileActivity
            Intent intent = new Intent(MainPageActivity.this, ProfileActivity.class);

            // Pass necessary data to ProfileActivity
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            intent.putExtra("profileImageUrl", profileImageUrl); // Pass profileImageUrl

            startActivity(intent); // Start the ProfileActivity
        });

        // Initialize SearchView and set listener for search query
        searchView = findViewById(R.id.search_view); // Get the SearchView from XML
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No action on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRestaurants(newText);
                return true;
            }
        });

        // Customize filter button
        customizeFilterButton();

        // Initialize Firestore and RecyclerView
        initializeFirestore();
        initializeRecyclerView();

        // Fetch restaurant data from Firestore
        fetchRestaurantsFromFirestore();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void customizeFilterButton() {
        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setBackgroundColor(Color.parseColor("#8BC34A"));

        // Set OnClickListener for the Filter button
        filterButton.setOnClickListener(v -> {
            // Add functionality for filter button
            // For example, you can show a dialog with filter options
            // or open a new activity to select filter options like cuisine or rating
            Toast.makeText(MainPageActivity.this, "Filter functionality to be implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.restaurantRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>(); // Initialize filtered list
        adapter = new RestaurantAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRestaurantsFromFirestore() {
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        restaurantList.clear();
                        if (task.getResult().isEmpty()) {
                            Log.d("Firestore", "No restaurants found.");
                            Toast.makeText(MainPageActivity.this, "No restaurants available.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    RestaurantItem restaurant = new RestaurantItem(
                                            document.getId(),
                                            document.getString("name"),
                                            document.getDouble("rating").floatValue(),
                                            document.getString("distance"),
                                            document.getString("cuisineType"),
                                            document.getString("imageUrl")
                                    );
                                    restaurantList.add(restaurant);
                                } catch (Exception e) {
                                    Log.e("Firestore", "Error mapping document: " + document.getId(), e);
                                }
                            }
                        }
                        // Initially show all restaurants
                        filteredList.addAll(restaurantList);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error fetching documents", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching data", e));
    }

    private void filterRestaurants(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            // If query is empty, show all restaurants
            filteredList.addAll(restaurantList);
        } else {
            // Filter the list based on the query (case-insensitive)
            for (RestaurantItem restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(restaurant);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
