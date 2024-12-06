package com.mobdeve.s21.grp4.mco_taftbites;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


public class MainPageActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    private List<RestaurantItem> restaurantList;
    private List<RestaurantItem> filteredList;
    private RestaurantAdapter adapter;
    private ImageView profileImage;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        // Get data passed from Login/Signup Activity
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");


        // Generate default profile image URL based on username
        String profileImageUrl = "https://avatar.iran.liara.run/username?username=" + username;


        // Initialize the profile image ImageView
        profileImage = findViewById(R.id.profileImage);  // Profile image view in main page


        // Load profile image dynamically using Picasso (either default or uploaded)
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


        // Initialize Firestore and RecyclerView
        initializeFirestore();
        initializeRecyclerView();


        // Fetch restaurant data from Firestore
        fetchRestaurantsFromFirestore();
    }


    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.restaurantRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();
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
                                    Toast.makeText(MainPageActivity.this, "Error fetching restaurant data.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        filteredList.addAll(restaurantList);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainPageActivity.this, "Error fetching restaurants", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(MainPageActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());
    }


    private void filterRestaurants(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(restaurantList);
        } else {
            for (RestaurantItem restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(restaurant);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
