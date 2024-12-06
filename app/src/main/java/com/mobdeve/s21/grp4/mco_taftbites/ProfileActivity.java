package com.mobdeve.s21.grp4.mco_taftbites;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {


    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button editButton; // Edit button to open the dialog


    private String username, email, profileImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Initialize views
        profileImage = findViewById(R.id.profile_image);  // Profile image
        profileName = findViewById(R.id.profile_name);    // Profile name
        profileEmail = findViewById(R.id.profile_email);  // Profile email
        editButton = findViewById(R.id.edit_button);      // Edit button


        // Get the data passed from MainPageActivity
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        profileImageUrl = getIntent().getStringExtra("profileImageUrl");


        // Set the profile data in UI
        profileName.setText(username);
        profileEmail.setText(email);
        Picasso.get().load(profileImageUrl).into(profileImage);


        // Set the back button functionality
        findViewById(R.id.back_button).setOnClickListener(v -> finish());


        // Set onClickListener for edit button to show the edit dialog
        editButton.setOnClickListener(v -> showEditProfileDialog());
    }


    private void showEditProfileDialog() {
        // Create an EditText for new username input
        final EditText usernameEditText = new EditText(this);
        usernameEditText.setText(username);  // Set the current username as default text


        // Create the dialog to allow user to edit the username and profile image
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Edit Profile");


        // Layout for dialog
        dialogBuilder.setView(usernameEditText);


        // Add a listener to handle user input
        dialogBuilder.setPositiveButton("Save", (dialog, which) -> {
            String newUsername = usernameEditText.getText().toString().trim();


            // Check if the user has entered a new username
            if (newUsername.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }


            // Update the username with the new one entered by the user
            username = newUsername;


            // Update the profile UI with the new username
            profileName.setText(username);


            // Save the changes to Firebase
            saveChangesToFirebase();
        });


        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing on cancel
            dialog.dismiss();
        });


        dialogBuilder.create().show();
    }


    // Save the profile updates to Firebase
    private void saveChangesToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Get the current user ID


        // Prepare the new user data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("username", username);  // Update the username in the Firestore document
        updatedData.put("profileImageUrl", profileImageUrl);  // Assuming the image URL stays the same for now


        // Save the data in the user document
        db.collection("users").document(userId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // After successful update, notify user and refresh UI
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();


                    // Update profileName in MainPageActivity with the new username
                    Intent intent = new Intent(ProfileActivity.this, MainPageActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("profileImageUrl", profileImageUrl); // Pass updated profile image URL
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}
