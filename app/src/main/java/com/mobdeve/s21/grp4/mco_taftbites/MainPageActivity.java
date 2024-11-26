package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    Button facebook_login;
    private FirebaseFirestore db;
    private List<RestaurantItem> restaurantList;
    private RestaurantAdapter adapter;
    CallbackManager callbackManager;

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
        Log.d("MainPageActivity", "Logged-in User: " + username + " (" + email + ")");

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
    }

    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.restaurantRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(this, restaurantList);
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
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error fetching documents", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching data", e));
    }
}
